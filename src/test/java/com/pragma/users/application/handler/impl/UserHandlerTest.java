package com.pragma.users.application.handler.impl;

import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.application.dto.LoginRequest;
import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;
import com.pragma.users.application.mapper.IJwtResponseMapper;
import com.pragma.users.application.mapper.IOwnerResponseMapper;
import com.pragma.users.domain.api.IAuthenticationServicePort;
import com.pragma.users.domain.api.IJwtServicePort;
import com.pragma.users.domain.api.IRoleServicePort;
import com.pragma.users.domain.api.IUserRoleServicePort;
import com.pragma.users.domain.api.IUserServicePort;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.UserRole;
import com.pragma.users.domain.model.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IJwtResponseMapper jwtResponseMapper;

    @Mock
    private IOwnerResponseMapper ownerResponseMapper;

    @Mock
    private IJwtServicePort jwtServicePort;

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IRoleServicePort roleServicePort;

    @Mock
    private IUserRoleServicePort userRoleServicePort;

    @Mock
    private IAuthenticationServicePort authenticationServicePort;

    @InjectMocks
    private UserHandler userHandler;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserRole> userRoleCaptor;

    private OwnerRequest ownerRequest;
    private User userModel;
    private User savedUser;
    private OwnerResponse ownerResponse;
    private Role ownerRole;
    private LoginRequest loginRequest;
    private User user;
    private JwtResponse jwtResponse;
    private Set<RoleName> roles;

    @BeforeEach
    void setUp() throws ParseException {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        roles = new HashSet<>();
        roles.add(RoleName.ADMIN);
        roles.add(RoleName.CLIENT);

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setDocumentId("123456789");
        user.setPhoneNumber("+1234567890");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setActive(true);

        jwtResponse = new JwtResponse();
        jwtResponse.setId(1L);
        jwtResponse.setFirstName("John");
        jwtResponse.setLastName("Doe");
        jwtResponse.setEmail("test@example.com");
        jwtResponse.setRoles(List.of("ADMIN", "CLIENT"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ownerRequest = new OwnerRequest();
        ownerRequest.setFirstName("John");
        ownerRequest.setLastName("Doe");
        ownerRequest.setDocumentId("123456789");
        ownerRequest.setPhoneNumber("+573001234567");
        ownerRequest.setBirthDate(sdf.parse("1990-01-15"));
        ownerRequest.setEmail("john.doe@email.com");
        ownerRequest.setPassword("SecurePass123");

        userModel = new User();
        userModel.setFirstName("John");
        userModel.setLastName("Doe");
        userModel.setDocumentId("123456789");
        userModel.setPhoneNumber("+573001234567");
        userModel.setBirthDate(LocalDate.of(1990, 1, 15));
        userModel.setEmail("john.doe@email.com");
        userModel.setPassword("encodedPassword");

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setDocumentId("123456789");
        savedUser.setPhoneNumber("+573001234567");
        savedUser.setBirthDate(LocalDate.of(1990, 1, 15));
        savedUser.setEmail("john.doe@email.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setActive(true);

        ownerResponse = new OwnerResponse();
        ownerResponse.setId(1L);
        ownerResponse.setFirstName("John");
        ownerResponse.setLastName("Doe");
        ownerResponse.setEmail("john.doe@email.com");

        ownerRole = new Role();
        ownerRole.setId(2L);
        ownerRole.setName(RoleName.OWNER);
    }

    @Test
    void testAuthenticate_Success() {
        String expectedToken = "jwt.token.123";

        doNothing().when(authenticationServicePort).authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        when(userServicePort.findByEmail(loginRequest.getEmail())).thenReturn(user);
        when(jwtResponseMapper.toJwtResponse(user)).thenReturn(jwtResponse);
        when(jwtServicePort.generateToken(user)).thenReturn(expectedToken);

        JwtResponse result = userHandler.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(expectedToken, result.getToken());
        assertTrue(result.getRoles().contains("ADMIN"));
        assertTrue(result.getRoles().contains("CLIENT"));
        assertEquals(2, result.getRoles().size());

        verify(authenticationServicePort, times(1)).authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        verify(userServicePort, times(1)).findByEmail(loginRequest.getEmail());
        verify(jwtResponseMapper, times(1)).toJwtResponse(user);
        verify(jwtServicePort, times(1)).generateToken(user);
    }

    @Test
    void testAuthenticate_WhenAuthenticationFails_ShouldThrowException() {
        doThrow(new RuntimeException("Invalid credentials"))
                .when(authenticationServicePort)
                .authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userHandler.authenticate(loginRequest));

        assertEquals("Invalid credentials", exception.getMessage());

        verify(authenticationServicePort, times(1)).authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        verify(userServicePort, never()).findByEmail(anyString());
        verify(jwtResponseMapper, never()).toJwtResponse(any());
        verify(jwtServicePort, never()).generateToken(any());
    }

    @Test
    void testAuthenticate_WhenUserNotFound_ShouldThrowException() {
        doNothing().when(authenticationServicePort).authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        when(userServicePort.findByEmail(loginRequest.getEmail()))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userHandler.authenticate(loginRequest));

        assertEquals("User not found", exception.getMessage());

        verify(authenticationServicePort, times(1)).authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        verify(userServicePort, times(1)).findByEmail(loginRequest.getEmail());
        verify(jwtResponseMapper, never()).toJwtResponse(any());
        verify(jwtServicePort, never()).generateToken(any());
    }

    @Test
    void testAuthenticate_WithNullLoginRequest_ShouldThrowException() {
        assertThrows(NullPointerException.class,
                () -> userHandler.authenticate(null));
    }

    @Test
    void testAuthenticate_WithNullEmail_ShouldThrowException() {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setPassword("password123");
        invalidRequest.setEmail(null);

        doThrow(new IllegalArgumentException("Email cannot be null"))
                .when(authenticationServicePort)
                .authenticate(null, "password123");

        assertThrows(IllegalArgumentException.class,
                () -> userHandler.authenticate(invalidRequest));
    }

    @Test
    void testAuthenticate_WithNullPassword_ShouldThrowException() {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("test@example.com");
        invalidRequest.setPassword(null);

        doThrow(new IllegalArgumentException("Password cannot be null"))
                .when(authenticationServicePort)
                .authenticate("test@example.com", null);

        assertThrows(IllegalArgumentException.class,
                () -> userHandler.authenticate(invalidRequest));
    }

    @Test
    void testAuthenticate_WithEmptyEmail_ShouldThrowException() {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("");
        invalidRequest.setPassword("password123");

        doThrow(new IllegalArgumentException("Email cannot be empty"))
                .when(authenticationServicePort)
                .authenticate("", "password123");

        assertThrows(IllegalArgumentException.class,
                () -> userHandler.authenticate(invalidRequest));
    }

    @Test
    void testAuthenticate_WithEmptyPassword_ShouldThrowException() {
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("test@example.com");
        invalidRequest.setPassword("");

        doThrow(new IllegalArgumentException("Password cannot be empty"))
                .when(authenticationServicePort)
                .authenticate("test@example.com", "");

        assertThrows(IllegalArgumentException.class,
                () -> userHandler.authenticate(invalidRequest));
    }

    @Test
    void testAuthenticate_WhenMapperReturnsNull_ShouldThrowException() {
        doNothing().when(authenticationServicePort).authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        when(userServicePort.findByEmail(loginRequest.getEmail())).thenReturn(user);
        when(jwtResponseMapper.toJwtResponse(user)).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> userHandler.authenticate(loginRequest));
    }

    @Test
    void testAuthenticate_WhenTokenGenerationFails_ShouldThrowException() {
        doNothing().when(authenticationServicePort).authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        when(userServicePort.findByEmail(loginRequest.getEmail())).thenReturn(user);
        when(jwtResponseMapper.toJwtResponse(user)).thenReturn(jwtResponse);
        when(jwtServicePort.generateToken(user)).thenThrow(new RuntimeException("Token generation failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userHandler.authenticate(loginRequest));

        assertEquals("Token generation failed", exception.getMessage());
    }

    @Test
    void testAuthenticate_WithDifferentEmailCase_ShouldWorkCorrectly() {
        String emailWithDifferentCase = "Test@Example.com";
        loginRequest.setEmail(emailWithDifferentCase);
        loginRequest.setPassword("password123");

        String expectedToken = "jwt.token.123";

        doNothing().when(authenticationServicePort).authenticate(
                emailWithDifferentCase,
                "password123"
        );

        when(userServicePort.findByEmail(emailWithDifferentCase)).thenReturn(user);
        when(jwtResponseMapper.toJwtResponse(user)).thenReturn(jwtResponse);
        when(jwtServicePort.generateToken(user)).thenReturn(expectedToken);

        JwtResponse result = userHandler.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals(expectedToken, result.getToken());
        verify(authenticationServicePort, times(1)).authenticate(
                emailWithDifferentCase,
                "password123"
        );
        verify(userServicePort, times(1)).findByEmail(emailWithDifferentCase);
    }

    @Test
    void testAuthenticate_WhenUserHasNoRoles_ShouldReturnEmptyRoles() {
        User userWithoutRoles = new User();
        userWithoutRoles.setId(2L);
        userWithoutRoles.setFirstName("Jane");
        userWithoutRoles.setLastName("Smith");
        userWithoutRoles.setEmail("jane@example.com");
        userWithoutRoles.setRoles(null);

        JwtResponse responseWithoutRoles = new JwtResponse();
        responseWithoutRoles.setId(2L);
        responseWithoutRoles.setFirstName("Jane");
        responseWithoutRoles.setLastName("Smith");
        responseWithoutRoles.setEmail("jane@example.com");
        responseWithoutRoles.setRoles(new ArrayList<>());

        String expectedToken = "jwt.token.456";

        doNothing().when(authenticationServicePort).authenticate(
                "jane@example.com",
                "password123"
        );

        LoginRequest request = new LoginRequest();
        request.setEmail("jane@example.com");
        request.setPassword("password123");

        when(userServicePort.findByEmail("jane@example.com")).thenReturn(userWithoutRoles);
        when(jwtResponseMapper.toJwtResponse(userWithoutRoles)).thenReturn(responseWithoutRoles);
        when(jwtServicePort.generateToken(userWithoutRoles)).thenReturn(expectedToken);

        JwtResponse result = userHandler.authenticate(request);

        assertNotNull(result);
        assertNotNull(result.getRoles());
        assertTrue(result.getRoles().isEmpty());
        assertEquals(expectedToken, result.getToken());
    }

    @Test
    void createOwner_Success_ShouldCreateOwnerWithRole() {
        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(ownerRequest.getPassword())).thenReturn("encodedPassword");
        when(userServicePort.save(any(User.class))).thenReturn(savedUser);
        when(ownerResponseMapper.toResponse(savedUser)).thenReturn(ownerResponse);
        when(roleServicePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRoleServicePort.save(any(UserRole.class))).thenReturn(new UserRole());

        OwnerResponse result = userHandler.createOwner(ownerRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@email.com", result.getEmail());
        assertTrue(result.getRoles().contains("OWNER"));

        verify(ownerResponseMapper, times(1)).toModel(ownerRequest);
        verify(authenticationServicePort, times(1)).encode(ownerRequest.getPassword());
        verify(userServicePort, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals("encodedPassword", capturedUser.getPassword());
        verify(roleServicePort, times(1)).getRoleByName(RoleName.OWNER);
        verify(userRoleServicePort, times(1)).save(userRoleCaptor.capture());
        UserRole capturedUserRole = userRoleCaptor.getValue();
        assertEquals(1L, capturedUserRole.getUserId());
        assertEquals(2L, capturedUserRole.getRoleId());
        verify(ownerResponseMapper, times(1)).toResponse(savedUser);
    }

    @Test
    void createOwner_WhenUserServiceFails_ShouldThrowException() {
        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(ownerRequest.getPassword())).thenReturn("encodedPassword");
        when(userServicePort.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userHandler.createOwner(ownerRequest));

        assertEquals("Database error", exception.getMessage());

        verify(roleServicePort, never()).getRoleByName(any());
        verify(userRoleServicePort, never()).save(any());
        verify(ownerResponseMapper, never()).toResponse(any());
    }

    @Test
    void createOwner_WhenRoleNotFound_ShouldThrowException() {
        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(ownerRequest.getPassword())).thenReturn("encodedPassword");
        when(userServicePort.save(any(User.class))).thenReturn(savedUser);
        when(roleServicePort.getRoleByName(RoleName.OWNER)).thenThrow(new RuntimeException("Role not found"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userHandler.createOwner(ownerRequest));

        assertEquals("Role not found", exception.getMessage());

        verify(userRoleServicePort, never()).save(any());
        verify(ownerResponseMapper, times(1)).toResponse(savedUser);
    }

    @Test
    void createOwner_WhenUserRoleSaveFails_ShouldThrowException() {
        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(ownerRequest.getPassword())).thenReturn("encodedPassword");
        when(userServicePort.save(any(User.class))).thenReturn(savedUser);
        when(ownerResponseMapper.toResponse(savedUser)).thenReturn(ownerResponse);
        when(roleServicePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRoleServicePort.save(any(UserRole.class))).thenThrow(new RuntimeException("Failed to save user role"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userHandler.createOwner(ownerRequest));

        assertEquals("Failed to save user role", exception.getMessage());
    }

    @Test
    void createOwner_VerifyPasswordEncoding() {
        String rawPassword = "SecurePass123";
        String encodedPassword = "bcrypt$encodedPassword123";

        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(rawPassword)).thenReturn(encodedPassword);
        when(userServicePort.save(any(User.class))).thenReturn(savedUser);
        when(ownerResponseMapper.toResponse(savedUser)).thenReturn(ownerResponse);
        when(roleServicePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRoleServicePort.save(any(UserRole.class))).thenReturn(new UserRole());

        userHandler.createOwner(ownerRequest);

        verify(authenticationServicePort, times(1)).encode(rawPassword);
        verify(userServicePort, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(encodedPassword, savedUser.getPassword());
    }

    @Test
    void createOwner_ShouldSetAllUserFieldsCorrectly() {
        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(ownerRequest.getPassword())).thenReturn("encodedPassword");
        when(userServicePort.save(any(User.class))).thenReturn(savedUser);
        when(ownerResponseMapper.toResponse(savedUser)).thenReturn(ownerResponse);
        when(roleServicePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRoleServicePort.save(any(UserRole.class))).thenReturn(new UserRole());

        userHandler.createOwner(ownerRequest);

        verify(userServicePort, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("John", capturedUser.getFirstName());
        assertEquals("Doe", capturedUser.getLastName());
        assertEquals("123456789", capturedUser.getDocumentId());
        assertEquals("+573001234567", capturedUser.getPhoneNumber());
        assertEquals(LocalDate.of(1990, 1, 15), capturedUser.getBirthDate());
        assertEquals("john.doe@email.com", capturedUser.getEmail());
    }

    @Test
    void createOwner_WithNullFirstName_ShouldPassThrough() {
        ownerRequest.setFirstName(null);
        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(ownerRequest.getPassword())).thenReturn("encodedPassword");
        when(userServicePort.save(any(User.class))).thenReturn(savedUser);
        when(ownerResponseMapper.toResponse(savedUser)).thenReturn(ownerResponse);
        when(roleServicePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRoleServicePort.save(any(UserRole.class))).thenReturn(new UserRole());

        OwnerResponse result = userHandler.createOwner(ownerRequest);

        assertNotNull(result);
        verify(userServicePort, times(1)).save(any(User.class));
    }

    @Test
    void createOwner_VerifyResponseAfterRoleAssignment() {
        when(ownerResponseMapper.toModel(ownerRequest)).thenReturn(userModel);
        when(authenticationServicePort.encode(ownerRequest.getPassword())).thenReturn("encodedPassword");
        when(userServicePort.save(any(User.class))).thenReturn(savedUser);
        when(ownerResponseMapper.toResponse(savedUser)).thenReturn(ownerResponse);
        when(roleServicePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRoleServicePort.save(any(UserRole.class))).thenReturn(new UserRole());

        OwnerResponse result = userHandler.createOwner(ownerRequest);

        assertNotNull(result);
        assertTrue(result.getRoles().contains("OWNER"));
        assertEquals(1, result.getRoles().size());
    }
}