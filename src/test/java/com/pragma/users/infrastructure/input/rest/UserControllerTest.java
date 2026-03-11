package com.pragma.users.infrastructure.input.rest;

import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;
import com.pragma.users.application.handler.IUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private UserController userController;

    private OwnerRequest ownerRequest;
    private OwnerResponse ownerResponse;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws ParseException {

        ownerRequest = new OwnerRequest();
        ownerRequest.setFirstName("John");
        ownerRequest.setLastName("Doe");
        ownerRequest.setDocumentId("123456789");
        ownerRequest.setPhoneNumber("+573001234567");
        ownerRequest.setBirthDate(sdf.parse("1990-01-15"));
        ownerRequest.setEmail("john.doe@email.com");
        ownerRequest.setPassword("SecurePass123");

        ownerResponse = new OwnerResponse();
        ownerResponse.setId(1L);
        ownerResponse.setFirstName("John");
        ownerResponse.setLastName("Doe");
        ownerResponse.setEmail("john.doe@email.com");
        ownerResponse.setRoles(List.of("OWNER"));
    }

    @Test
    void createOwner_WithValidRequest_ShouldReturnOkWithResponse() {
        when(userHandler.createOwner(any(OwnerRequest.class))).thenReturn(ownerResponse);

        ResponseEntity<OwnerResponse> response = userController.createOwner(ownerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        OwnerResponse body = response.getBody();
        assertEquals(1L, body.getId());
        assertEquals("John", body.getFirstName());
        assertEquals("Doe", body.getLastName());
        assertEquals("john.doe@email.com", body.getEmail());
        assertTrue(body.getRoles().contains("OWNER"));

        verify(userHandler, times(1)).createOwner(ownerRequest);
    }

    @Test
    void createOwner_WhenHandlerReturnsResponse_ShouldReturnSameResponse() {
        when(userHandler.createOwner(any(OwnerRequest.class))).thenReturn(ownerResponse);

        ResponseEntity<OwnerResponse> response = userController.createOwner(ownerRequest);

        assertNotNull(response.getBody());
        assertEquals(ownerResponse.getId(), response.getBody().getId());
        assertEquals(ownerResponse.getFirstName(), response.getBody().getFirstName());
        assertEquals(ownerResponse.getLastName(), response.getBody().getLastName());
        assertEquals(ownerResponse.getEmail(), response.getBody().getEmail());
        assertEquals(ownerResponse.getRoles(), response.getBody().getRoles());
    }

    @Test
    void createOwner_WhenHandlerThrowsException_ShouldPropagateException() {
        String errorMessage = "Error creating owner";
        when(userHandler.createOwner(any(OwnerRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userController.createOwner(ownerRequest));

        assertEquals(errorMessage, exception.getMessage());
        verify(userHandler, times(1)).createOwner(ownerRequest);
    }

    @Test
    void createOwner_WithNullRequest_ShouldPassNullToHandler() {
        when(userHandler.createOwner(null)).thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class,
                () -> userController.createOwner(null));

        verify(userHandler, times(1)).createOwner(null);
    }
}