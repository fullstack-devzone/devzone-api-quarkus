package com.sivalabs.devzone.links.web.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class TagControllerTest {

    @Test
    void getAllTags() {
        given()
                .when().get("/api/tags")
                .then()
                .statusCode(200)
                .assertThat()
                .body("$.size()", greaterThan(0))
        ;
    }
}