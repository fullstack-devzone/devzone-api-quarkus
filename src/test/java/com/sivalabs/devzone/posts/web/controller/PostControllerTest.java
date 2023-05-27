package com.sivalabs.devzone.posts.web.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import com.sivalabs.devzone.posts.models.PostDTO;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestHTTPEndpoint(PostController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostControllerTest {

    @Test
    @Order(1)
    public void getPosts() {
        given().when()
                .get("?page=1")
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
                .body("data.size()", equalTo(10));
    }

    @Test
    @Order(2)
    public void searchPosts() {
        given().when()
                .get("?query=spring")
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
                .body("data.size()", equalTo(10));
    }

    @Test
    @Order(3)
    public void getPostById() {
        given().when().get("/1").then().statusCode(200);
    }

    @Test
    @Order(4)
    @TestSecurity(
            user = "siva",
            roles = {"ROLE_USER"})
    @JwtSecurity(claims = {@Claim(key = "email", value = "siva@gmail.com")})
    public void createPost() {
        PostDTO postDTO = given().body(
                        """
                                {
                                    "title": "SivaLabs Blog",
                                    "url": "https://sivalabs.in",
                                    "content": "java blog"
                                }
                                """)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .as(PostDTO.class);
    }

    @Test
    @Order(5)
    @TestSecurity(
            user = "admin",
            roles = {"ROLE_ADMIN"})
    @JwtSecurity(claims = {@Claim(key = "email", value = "admin@gmail.com")})
    public void deletePostById() {
        Long id = 1L;
        given().when().delete("/{id}", id).then().statusCode(204);
    }
}
