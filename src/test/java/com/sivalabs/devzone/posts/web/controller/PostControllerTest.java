package com.sivalabs.devzone.posts.web.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import com.sivalabs.devzone.posts.models.CreatePostRequest;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.services.PostService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(PostController.class)
public class PostControllerTest {

    @Inject
    PostService postService;

    @Test
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
    public void getPostById() {
        PostDTO post = createTestPost();

        given().when().get("/{id}", post.getId()).then().statusCode(200);
    }

    @Test
    @TestSecurity(
            user = "siva",
            roles = {"ROLE_USER"})
    @JwtSecurity(claims = {@Claim(key = "email", value = "siva@gmail.com")})
    public void createPost() {
        given().body(
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
    @TestSecurity(
            user = "admin",
            roles = {"ROLE_ADMIN"})
    @JwtSecurity(claims = {@Claim(key = "email", value = "admin@gmail.com")})
    public void deletePostById() {
        PostDTO post = createTestPost();
        given().when().delete("/{id}", post.getId()).then().statusCode(204);
    }

    private PostDTO createTestPost() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setTitle("title");
        createPostRequest.setUrl("url");
        createPostRequest.setContent("content");
        createPostRequest.setUserId(1L);
        return postService.createPost(createPostRequest);
    }
}
