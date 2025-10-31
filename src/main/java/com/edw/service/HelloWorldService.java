package com.edw.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

/**
 * <pre>
 *  com.edw.service.HelloWorldService
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 31 Oct 2025 16:41
 */
@Service
public class HelloWorldService {

    @Value("${secretKey}")
    private String secretKey;

    private Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

    private HttpServletRequest httpServletRequest;

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void sayHello() {
        String jwtToken = httpServletRequest.getHeader("my_token");
        String username = getUsername(jwtToken);
        logger.info("Hello {}", username);
    }

    private String getUsername(String jwtToken) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        return (String) claims.get("username");
    }

}
