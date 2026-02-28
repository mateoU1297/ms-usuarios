package com.pragma.usuarios.infrastructure.config.security;

import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserEntity userEntity;
    private final String VALID_TOKEN = "valid.jwt.token";
    private final String EMAIL = "test@example.com";
    private final String AUTHORIZATION_HEADER = "Bearer " + VALID_TOKEN;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail(EMAIL);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setActive(true);
    }

    @Test
    void doFilterInternal_WithValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(AUTHORIZATION_HEADER);
        when(jwtUtils.validateJwtToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtils.getEmailFromJwtToken(VALID_TOKEN)).thenReturn(EMAIL);
        when(userRepository.findByEmailWithRoles(EMAIL)).thenReturn(Optional.of(userEntity));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(EMAIL,
                SecurityContextHolder.getContext().getAuthentication().getName());

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtils, times(1)).validateJwtToken(VALID_TOKEN);
        verify(jwtUtils, times(1)).getEmailFromJwtToken(VALID_TOKEN);
        verify(userRepository, times(1)).findByEmailWithRoles(EMAIL);
    }

    @Test
    void doFilterInternal_WithNoAuthorizationHeader_ShouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(jwtUtils, never()).getEmailFromJwtToken(anyString());
        verify(userRepository, never()).findByEmailWithRoles(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithEmptyAuthorizationHeader_ShouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidBearerFormat_ShouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic " + VALID_TOKEN);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        String INVALID_TOKEN = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + INVALID_TOKEN);
        when(jwtUtils.validateJwtToken(INVALID_TOKEN)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, times(1)).validateJwtToken(INVALID_TOKEN);
        verify(jwtUtils, never()).getEmailFromJwtToken(anyString());
        verify(userRepository, never()).findByEmailWithRoles(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenValidateTokenThrowsException_ShouldHandleGracefully() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(AUTHORIZATION_HEADER);
        when(jwtUtils.validateJwtToken(VALID_TOKEN)).thenThrow(new RuntimeException("JWT validation error"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, times(1)).validateJwtToken(VALID_TOKEN);
        verify(jwtUtils, never()).getEmailFromJwtToken(anyString());
        verify(userRepository, never()).findByEmailWithRoles(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenUserNotFound_ShouldNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(AUTHORIZATION_HEADER);
        when(jwtUtils.validateJwtToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtils.getEmailFromJwtToken(VALID_TOKEN)).thenReturn(EMAIL);
        when(userRepository.findByEmailWithRoles(EMAIL)).thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, times(1)).validateJwtToken(VALID_TOKEN);
        verify(jwtUtils, times(1)).getEmailFromJwtToken(VALID_TOKEN);
        verify(userRepository, times(1)).findByEmailWithRoles(EMAIL);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenGetEmailThrowsException_ShouldHandleGracefully() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(AUTHORIZATION_HEADER);
        when(jwtUtils.validateJwtToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtils.getEmailFromJwtToken(VALID_TOKEN)).thenThrow(new RuntimeException("Error extracting email"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, times(1)).validateJwtToken(VALID_TOKEN);
        verify(jwtUtils, times(1)).getEmailFromJwtToken(VALID_TOKEN);
        verify(userRepository, never()).findByEmailWithRoles(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenFilterChainThrowsException_ShouldPropagate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        doThrow(new IOException("Network error")).when(filterChain).doFilter(request, response);

        assertThrows(IOException.class, () ->
                jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
    }
}