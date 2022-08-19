package com.sivalabs.devzone.links.web.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
class PageMetadataControllerTest {

    @Test
    void getPageMetadata() {
        given()
                .when().get("/api/page-metadata?url=https://sivalabs.in")
                .then()
                .statusCode(200)
                .assertThat()
                .body("title", equalTo("SivaLabs - My Experiments with Technology"))
        ;
    }
}