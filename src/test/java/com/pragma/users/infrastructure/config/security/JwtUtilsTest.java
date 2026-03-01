package com.pragma.users.infrastructure.config.security;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.enums.RoleName;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private final String SECRET = "mySecretKeyForJwtTokenGenerationAndValidationThatIsLongEnough";
    private final int EXPIRATION_MS = 3600000;
    private final String INVALID_TOKEN = "invalid.token.string";

    private User user;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", EXPIRATION_MS);

        Set<RoleName> roles = new HashSet<>();
        roles.add(RoleName.ADMIN);
        roles.add(RoleName.CLIENT);

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRoles(Set.of(createRole(RoleName.ADMIN), createRole(RoleName.CLIENT)));
    }

    private Role createRole(RoleName name) {
        Role role = new Role();
        role.setId(name == RoleName.ADMIN ? 1L : 2L);
        role.setName(name);
        role.setDescription(name.name());
        return role;
    }

    @Test
    void generateJwtToken_WithValidUser_ShouldGenerateToken() {
        String token = jwtUtils.generateJwtToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);

        String email = jwtUtils.getEmailFromJwtToken(token);
        assertEquals(user.getEmail(), email);
    }

    @Test
    void generateJwtToken_WithUserHavingMultipleRoles_ShouldIncludeAllRoles() {
        String token = jwtUtils.generateJwtToken(user);

        assertNotNull(token);

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String rolesClaim = claims.get("roles", String.class);
        assertNotNull(rolesClaim);
        assertTrue(rolesClaim.contains("ADMIN"));
        assertTrue(rolesClaim.contains("CLIENT"));
    }

    @Test
    void generateJwtToken_WithUserHavingSingleRole_ShouldIncludeThatRole() {
        user.setRoles(Set.of(createRole(RoleName.ADMIN)));

        String token = jwtUtils.generateJwtToken(user);

        assertNotNull(token);

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String rolesClaim = claims.get("roles", String.class);
        assertEquals("ROLE_ADMIN", rolesClaim);
    }

    @Test
    void generateJwtToken_WithUserHavingNoRoles_ShouldGenerateTokenWithEmptyRoles() {
        user.setRoles(new HashSet<>());

        String token = jwtUtils.generateJwtToken(user);

        assertNotNull(token);

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String rolesClaim = claims.get("roles", String.class);
        assertEquals("", rolesClaim);
    }

    @Test
    void generateJwtToken_ShouldIncludeUserDetails() {
        String token = jwtUtils.generateJwtToken(user);

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(user.getId().intValue(), claims.get("id", Integer.class));
        assertEquals(user.getFirstName(), claims.get("firstName"));
        assertEquals(user.getLastName(), claims.get("lastName"));
        assertEquals(user.getEmail(), claims.getSubject());
    }

    @Test
    void generateJwtToken_ShouldSetIssuedAtAndExpirationDates() {
        String token = jwtUtils.generateJwtToken(user);
        Date now = new Date();

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        assertNotNull(issuedAt);
        assertNotNull(expiration);
        assertTrue(issuedAt.before(expiration) || issuedAt.equals(expiration));
        assertTrue(expiration.after(now));
        assertEquals(EXPIRATION_MS, expiration.getTime() - issuedAt.getTime());
    }

    @Test
    void getEmailFromJwtToken_WithValidToken_ShouldReturnEmail() {
        String token = jwtUtils.generateJwtToken(user);

        String email = jwtUtils.getEmailFromJwtToken(token);

        assertEquals(user.getEmail(), email);
    }

    @Test
    void getEmailFromJwtToken_WithInvalidToken_ShouldThrowException() {
        assertThrows(Exception.class, () ->
                jwtUtils.getEmailFromJwtToken(INVALID_TOKEN));
    }

    @Test
    void validateJwtToken_WithValidToken_ShouldReturnTrue() {
        String token = jwtUtils.generateJwtToken(user);

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_WithMalformedToken_ShouldReturnFalse() {
        String MALFORMED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";
        boolean isValid = jwtUtils.validateJwtToken(MALFORMED_TOKEN);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_WithInvalidToken_ShouldReturnFalse() {
        boolean isValid = jwtUtils.validateJwtToken(INVALID_TOKEN);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_WithNullToken_ShouldReturnFalse() {
        boolean isValid = jwtUtils.validateJwtToken(null);

        assertFalse(isValid);
    }

    @Test
    void generateJwtToken_WithNullUser_ShouldThrowException() {
        assertThrows(NullPointerException.class, () ->
                jwtUtils.generateJwtToken(null));
    }

    @Test
    void validateJwtToken_WithTokenContainingInvalidClaims_ShouldReturnFalse() {
        String tokenWithoutSignature = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";

        boolean isValid = jwtUtils.validateJwtToken(tokenWithoutSignature);

        assertFalse(isValid);
    }
}