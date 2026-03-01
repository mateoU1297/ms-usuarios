package com.pragma.users.infrastructure.config.security.adapter;

import com.pragma.users.domain.spi.IAuthenticationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationAdapter implements IAuthenticationPort {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            log.error("Bad credentials for email: {}", email);
            throw new BadCredentialsException("Invalid email or password");
        } catch (DisabledException e) {
            log.error("User disabled: {}", email);
            throw new DisabledException("User account is disabled");
        } catch (LockedException e) {
            log.error("User locked: {}", email);
            throw new LockedException("User account is locked");
        } catch (AuthenticationException e) {
            log.error("Authentication failed for email: {} - {}", email, e.getMessage());
            throw new AuthenticationException("Authentication failed: " + e.getMessage()) {
            };
        }
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
