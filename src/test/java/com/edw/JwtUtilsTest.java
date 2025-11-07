package com.edw;

import com.edw.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <pre>
 *  com.edw.JwtUtilsTest
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 31 Oct 2025 16:19
 */
public class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String testUsername = "testuser";
    private final String secretKey = "oclmpoldexmy49moueb30lxncdqyvba5xxsw5mk9y664aofwyvrj6a0ibul03jqa";

    @BeforeEach
    public void setup() {
        jwtUtils = new JwtUtils();
        // We need to set the private fields using reflection since they don't have setters
        ReflectionTestUtils.setField(jwtUtils, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtUtils, "loginJwtExpiration", 3600000);
        ReflectionTestUtils.setField(jwtUtils, "loginJwtRefreshExpiration", 86400000);
    }

    @Test
    public void testGenerateJwtToken() {
        String token = jwtUtils.generateJwtToken(testUsername);

        // print our token for debugging
        System.out.println(token);

        // Verify token is not null or empty
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verify token can be parsed and contains expected claims
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(testUsername, claims.getSubject());
        assertEquals(testUsername, claims.get("username"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    public void testValidateJwtToken_ValidToken() {
        String token = jwtUtils.generateJwtToken(testUsername);
        boolean isValid = jwtUtils.validateJwtToken(token);
        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtToken_InvalidToken() {
        // Test with malformed token
        String invalidToken = "invalid.jwt.token";
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_EmptyToken() {
        // Test with empty token
        boolean isValid = jwtUtils.validateJwtToken("");
        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_ExpiredToken() {
        // Create an expired token manually
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        String expiredToken = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date(System.currentTimeMillis() - 3600000)) // 1 hour ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired 1 second ago
                .signWith(key)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);
        assertFalse(isValid);
    }
}
