package com.edw.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * <pre>
 *  com.edw.utils.JwtUtils
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 31 Oct 2025 15:57
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${secretKey}")
    private String secretKey;
    private Integer loginJwtExpiration = 3600000;
    private Integer loginJwtRefreshExpiration = 86400000;

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + Long.parseLong("" + loginJwtExpiration)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
