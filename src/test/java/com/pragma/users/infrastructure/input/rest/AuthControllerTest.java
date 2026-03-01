package com.pragma.users.infrastructure.input.rest;

import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.application.dto.LoginRequest;
import com.pragma.users.application.handler.IUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private AuthController authController;

    private LoginRequest validLoginRequest;
    private JwtResponse validJwtResponse;
    private final String VALID_EMAIL = "test@example.com";
    private final String VALID_PASSWORD = "password123";
    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail(VALID_EMAIL);
        validLoginRequest.setPassword(VALID_PASSWORD);

        validJwtResponse = new JwtResponse();
        validJwtResponse.setId(1L);
        validJwtResponse.setFirstName("John");
        validJwtResponse.setLastName("Doe");
        validJwtResponse.setEmail(VALID_EMAIL);
        validJwtResponse.setRoles(List.of("ADMIN", "CLIENT"));
        validJwtResponse.setToken(VALID_TOKEN);
    }

    @Test
    void authenticateUser_WithValidCredentials_ShouldReturnOkWithToken() {
        when(userHandler.authenticate(validLoginRequest)).thenReturn(validJwtResponse);

        ResponseEntity<JwtResponse> response = authController.authenticateUser(validLoginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        JwtResponse body = response.getBody();
        assertEquals(1L, body.getId());
        assertEquals("John", body.getFirstName());
        assertEquals("Doe", body.getLastName());
        assertEquals(VALID_EMAIL, body.getEmail());
        assertEquals(VALID_TOKEN, body.getToken());
        assertTrue(body.getRoles().contains("ADMIN"));
        assertTrue(body.getRoles().contains("CLIENT"));
        assertEquals(2, body.getRoles().size());

        verify(userHandler, times(1)).authenticate(validLoginRequest);
    }

    @Test
    void authenticateUser_WithValidCredentialsAndUserHasSingleRole_ShouldReturnOk() {
        JwtResponse singleRoleResponse = new JwtResponse();
        singleRoleResponse.setId(2L);
        singleRoleResponse.setFirstName("Jane");
        singleRoleResponse.setLastName("Smith");
        singleRoleResponse.setEmail("jane@example.com");
        singleRoleResponse.setRoles(List.of("CLIENT"));
        singleRoleResponse.setToken(VALID_TOKEN);

        LoginRequest request = new LoginRequest();
        request.setEmail("jane@example.com");
        request.setPassword("password456");

        when(userHandler.authenticate(request)).thenReturn(singleRoleResponse);

        ResponseEntity<JwtResponse> response = authController.authenticateUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        JwtResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Jane", body.getFirstName());
        assertEquals("Smith", body.getLastName());
        assertEquals(List.of("CLIENT"), body.getRoles());
        assertEquals(1, body.getRoles().size());

        verify(userHandler, times(1)).authenticate(request);
    }

    @Test
    void authenticateUser_WhenHandlerThrowsRuntimeException_ShouldPropagateException() {
        String errorMessage = "Authentication failed: Invalid credentials";
        when(userHandler.authenticate(validLoginRequest))
                .thenThrow(new RuntimeException(errorMessage));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authController.authenticateUser(validLoginRequest));

        assertEquals(errorMessage, exception.getMessage());
        verify(userHandler, times(1)).authenticate(validLoginRequest);
    }

    @Test
    void authenticateUser_VerifyResponseStructure() {
        when(userHandler.authenticate(validLoginRequest)).thenReturn(validJwtResponse);

        ResponseEntity<JwtResponse> response = authController.authenticateUser(validLoginRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getHeaders());
        assertTrue(response.getHeaders().isEmpty());

        JwtResponse body = response.getBody();
        assertNotNull(body.getId());
        assertNotNull(body.getFirstName());
        assertNotNull(body.getLastName());
        assertNotNull(body.getEmail());
        assertNotNull(body.getToken());
        assertNotNull(body.getRoles());
    }

    @Test
    void authenticateUser_VerifyHandlerInteraction() {
        when(userHandler.authenticate(validLoginRequest)).thenReturn(validJwtResponse);

        authController.authenticateUser(validLoginRequest);

        verify(userHandler, times(1)).authenticate(argThat(request ->
                request.getEmail().equals(VALID_EMAIL) &&
                        request.getPassword().equals(VALID_PASSWORD)
        ));
    }
}