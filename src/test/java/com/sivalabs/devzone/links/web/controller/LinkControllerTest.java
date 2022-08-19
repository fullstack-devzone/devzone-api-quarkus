package com.sivalabs.devzone.links.web.controller;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
@TestHTTPEndpoint(LinkController.class)
public class LinkControllerTest {

    @Test
    public void getLinks() {
        given()
          .when().get("?page=1")
          .then().statusCode(200)
          .assertThat()
                .body("totalElements", greaterThan(0))
                .body("totalPages", greaterThan(0))
                .body("pageNumber", equalTo(1))
                .body("isFirst", equalTo(true))
                .body("isLast", equalTo(false))
                .body("hasNext", equalTo(true))
                .body("hasPrevious", equalTo(false))
                .body("data.size()", equalTo(10))
        ;
    }

    @Test
    public void searchLinks() {
        given()
                .when().get("?query=spring")
                .then()
                .statusCode(200)
                .assertThat()
                .body("totalElements", greaterThan(0))
                .body("totalPages", greaterThan(0))
                .body("pageNumber", equalTo(1))
                .body("isFirst", equalTo(true))
                .body("isLast", equalTo(false))
                .body("hasNext", equalTo(true))
                .body("hasPrevious", equalTo(false))
                .body("data.size()", equalTo(10))
                ;
    }

    @Test
    public void getLinksByTag() {
        given()
                .when().get("?tag=java")
                .then()
                .statusCode(200)
                .assertThat()
                .body("totalElements", greaterThan(0))
                .body("totalPages", greaterThan(0))
                .body("pageNumber", equalTo(1))
                .body("isFirst", equalTo(true))
                .body("isLast", equalTo(false))
                .body("hasNext", equalTo(true))
                .body("hasPrevious", equalTo(false))
                .body("data.size()", equalTo(10))
        ;
    }

    @Test
    public void getLinkById() {
        given()
                .when().get("/1")
                .then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "siva", roles = {"ROLE_USER"})
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "siva@gmail.com")
    })
    public void createLink() {
        given()
                .body("""
                        {
                            "title": "SivaLabs Blog",
                            "url": "https://sivalabs.in",
                            "tags": ["java", "spring-boot"]
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().post()
                .then().statusCode(201);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ROLE_ADMIN"})
    @JwtSecurity(claims = {
            @Claim(key = "email", value = "admin@gmail.com")
    })
    public void deleteLinkById() {
        given()
                .when().delete("/1")
                .then().statusCode(204);
    }
}