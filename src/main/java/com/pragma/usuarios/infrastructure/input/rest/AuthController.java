package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.JwtResponse;
import com.pragma.usuarios.application.dto.LoginRequest;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.infrastructure.adapter.in.rest.api.AuthenticationApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthenticationApi {

    private final IUserHandler userHandler;

    @Override
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        log.info("Authenticating user {}", loginRequest.getEmail());
        JwtResponse response = userHandler.login(loginRequest);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/logout")
//    @Operation(summary = "Logout user")
//    public ResponseEntity<MessageResponse> logoutUser() {
//        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
//    }
//
//    @PostMapping("/validate")
//    @Operation(summary = "Validate JWT token")
//    public ResponseEntity<MessageResponse> validateToken(@RequestHeader("Authorization") String token) {
//        boolean isValid = authenticationService.validateToken(token.replace("Bearer ", ""));
//        if (isValid) {
//            return ResponseEntity.ok(new MessageResponse("Token is valid"));
//        } else {
//            return ResponseEntity.badRequest().body(new MessageResponse("Token is invalid"));
//        }
//    }
}
