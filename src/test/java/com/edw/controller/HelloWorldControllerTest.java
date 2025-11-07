package com.edw.controller;

import com.edw.utils.JwtUtils;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

/**
 * <pre>
 *  com.edw.controller.HelloWorldController
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 07 Nov 2025 21:59
 */
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class HelloWorldControllerTest {

    @LocalServerPort
    private int port;

    private JwtUtils jwtUtils;

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = this.port;
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secretKey", "oclmpoldexmy49moueb30lxncdqyvba5xxsw5mk9y664aofwyvrj6a0ibul03jqa");
        ReflectionTestUtils.setField(jwtUtils, "loginJwtExpiration", 3600000);
        ReflectionTestUtils.setField(jwtUtils, "loginJwtRefreshExpiration", 86400000);
    }

    @Test
    @DisplayName("01. Test Hello World Page without JWT token should give http 500")
    public void testHelloWorld_withoutJWT() {
        given()
            .when()
                .get("/")
            .then()
                .statusCode(500)
                .log().all();
    }

    @Test
    @DisplayName("02. Test Hello World Page with JWT token should give http 200")
    public void testHelloWorld_withJWT() {
        String token = jwtUtils.generateJwtToken("testuser");
        given()
                .header("my_token", token)
            .when()
                .get("/")
            .then()
                .statusCode(200)
                .body("message", isA(String.class))
                .body("message", equalTo("Hello World!"))
                .log().all();
    }

}
