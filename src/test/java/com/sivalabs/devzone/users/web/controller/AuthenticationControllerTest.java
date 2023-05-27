package com.sivalabs.devzone.users.web.controller;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AuthenticationControllerTest {
    @Test
    public void login() {
        given().body(
                        """
                        {
                            "username": "admin@gmail.com",
                            "password": "admin"
                        }
                        """)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("access_token", Matchers.notNullValue());
    }

    @Test
    public void invalidLogin() {
        given().body(
                        """
                        {
                            "username": "admin@gmail.com",
                            "password": "pwd"
                        }
                        """)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .then()
                .statusCode(401);
    }
}
