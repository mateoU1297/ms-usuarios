package com.pragma.users.domain.usecase;

import com.pragma.users.domain.exception.UserUnderageException;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.UserRole;
import com.pragma.users.domain.model.enums.RoleName;
import com.pragma.users.domain.spi.IAuthenticationPort;
import com.pragma.users.domain.spi.IRolePersistencePort;
import com.pragma.users.domain.spi.IUserPersistencePort;
import com.pragma.users.domain.spi.IUserRolePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IAuthenticationPort authenticationPort;
    @Mock
    private IRolePersistencePort rolePersistencePort;
    @Mock
    private IUserRolePersistencePort userRolePersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User adultUser;
    private User minorUser;
    private Role ownerRole;

    @BeforeEach
    void setUp() {
        adultUser = new User();
        adultUser.setId(1L);
        adultUser.setEmail("owner@mail.com");
        adultUser.setPassword("plainPassword");
        adultUser.setBirthDate(LocalDate.now().minusYears(25));

        minorUser = new User();
        minorUser.setEmail("minor@mail.com");
        minorUser.setPassword("plainPassword");
        minorUser.setBirthDate(LocalDate.now().minusYears(16));

        ownerRole = new Role();
        ownerRole.setId(1L);
        ownerRole.setName(RoleName.OWNER);
    }

    @Test
    void save_whenUserIsAdult_shouldEncodePasswordAndAssignRole() {
        when(authenticationPort.encode("plainPassword")).thenReturn("encodedPassword");
        when(userPersistencePort.save(any(User.class))).thenReturn(adultUser);
        when(rolePersistencePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRolePersistencePort.save(any(UserRole.class))).thenReturn(new UserRole());

        User result = userUseCase.save(adultUser, RoleName.OWNER);

        verify(authenticationPort).encode("plainPassword");
        verify(userPersistencePort).save(adultUser);

        verify(rolePersistencePort).getRoleByName(RoleName.OWNER);
        verify(userRolePersistencePort).save(any(UserRole.class));

        assertNotNull(result);
        assertTrue(result.getRoles().contains(ownerRole));
    }

    @Test
    void save_whenUserIsAdult_shouldPersistUserRoleWithCorrectIds() {
        when(authenticationPort.encode(any())).thenReturn("encodedPassword");
        when(userPersistencePort.save(any())).thenReturn(adultUser);
        when(rolePersistencePort.getRoleByName(RoleName.OWNER)).thenReturn(ownerRole);
        when(userRolePersistencePort.save(any())).thenReturn(new UserRole());

        userUseCase.save(adultUser, RoleName.OWNER);

        verify(userRolePersistencePort).save(argThat(userRole ->
                userRole.getUserId().equals(1L) &&
                        userRole.getRoleId().equals(1L)
        ));
    }

    @Test
    void save_whenUserIsMinor_shouldThrowUserUnderageException() {
        assertThrows(UserUnderageException.class,
                () -> userUseCase.save(minorUser, RoleName.OWNER));

        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(authenticationPort);
        verifyNoInteractions(rolePersistencePort);
        verifyNoInteractions(userRolePersistencePort);
    }

    @Test
    void save_whenUserIsMinor_shouldNotEncodePassword() {
        assertThrows(UserUnderageException.class,
                () -> userUseCase.save(minorUser, RoleName.OWNER));

        verify(authenticationPort, never()).encode(any());
    }

    @Test
    void findByEmail_shouldReturnUserFromPersistence() {
        when(userPersistencePort.findByEmail("owner@mail.com")).thenReturn(adultUser);

        User result = userUseCase.findByEmail("owner@mail.com");

        assertEquals(adultUser, result);
        verify(userPersistencePort).findByEmail("owner@mail.com");
    }

    @Test
    void findById_shouldReturnUserFromPersistence() {
        when(userPersistencePort.findById(1L)).thenReturn(adultUser);

        User result = userUseCase.findById(1L);

        assertEquals(adultUser, result);
        verify(userPersistencePort).findById(1L);
    }
}