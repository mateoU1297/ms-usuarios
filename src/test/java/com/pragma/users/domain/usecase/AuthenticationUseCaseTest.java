package com.pragma.users.domain.usecase;

import com.pragma.users.domain.model.AuthResult;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.IAuthenticationPort;
import com.pragma.users.domain.spi.IJwtPort;
import com.pragma.users.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationUseCaseTest {

    @Mock
    private IAuthenticationPort authenticationPort;
    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IJwtPort jwtPort;

    @InjectMocks
    private AuthenticationUseCase authenticationUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("owner@mail.com");
    }

    @Test
    void authenticate_whenCredentialsAreValid_shouldReturnAuthResultWithTokenAndUser() {
        when(userPersistencePort.findByEmail("owner@mail.com")).thenReturn(user);
        when(jwtPort.generateToken(user)).thenReturn("jwt.token.here");

        AuthResult result = authenticationUseCase.authenticate("owner@mail.com", "password");

        assertNotNull(result);
        assertEquals("jwt.token.here", result.getToken());
        assertEquals(user, result.getUser());
    }

    @Test
    void authenticate_shouldAuthenticateBeforeFetchingUser() {
        when(userPersistencePort.findByEmail(any())).thenReturn(user);
        when(jwtPort.generateToken(any())).thenReturn("token");

        authenticationUseCase.authenticate("owner@mail.com", "password");

        InOrder inOrder = inOrder(authenticationPort, userPersistencePort, jwtPort);
        inOrder.verify(authenticationPort).authenticate("owner@mail.com", "password");
        inOrder.verify(userPersistencePort).findByEmail("owner@mail.com");
        inOrder.verify(jwtPort).generateToken(user);
    }

    @Test
    void authenticate_whenAuthenticationFails_shouldThrowAndNotFetchUser() {
        doThrow(new RuntimeException("Bad credentials"))
                .when(authenticationPort).authenticate("owner@mail.com", "wrongPassword");

        assertThrows(RuntimeException.class,
                () -> authenticationUseCase.authenticate("owner@mail.com", "wrongPassword"));

        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(jwtPort);
    }

    @Test
    void authenticate_shouldGenerateTokenWithCorrectUser() {
        when(userPersistencePort.findByEmail("owner@mail.com")).thenReturn(user);
        when(jwtPort.generateToken(user)).thenReturn("jwt.token.here");

        authenticationUseCase.authenticate("owner@mail.com", "password");

        verify(jwtPort).generateToken(user);
    }
}