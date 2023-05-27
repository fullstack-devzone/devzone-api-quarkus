package com.sivalabs.devzone.posts.web.controller;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.models.PostsDTO;
import com.sivalabs.devzone.posts.services.PostService;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final JsonWebToken jwt;

    @GET
    @PermitAll
    public PostsDTO findAllPosts(
            @QueryParam("page") @DefaultValue("1") int page, @QueryParam("query") @DefaultValue("") String query) {
        if (StringUtils.isNotEmpty(query)) {
            log.info("Searching posts for {} with page: {}", query, page);
            return postService.searchPosts(query, page);
        } else {
            log.info("Fetching posts with page: {}", page);
            return postService.getAllPosts(page);
        }
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public PostDTO getPost(@PathParam("id") Long id) {
        return postService
                .getPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found"));
    }

    @POST
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public Response createPost(@Valid PostDTO post, @Context UriInfo uriInfo, @Context SecurityContext ctx) {
        String email = jwt.getClaim("email");
        User user = userService.getUserByEmail(email).orElseThrow();
        post.setCreatedUserId(user.getId());
        PostDTO postDTO = postService.createPost(post);
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(Long.toString(postDTO.getId()))
                .build();
        return Response.created(uri).entity(postDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public Response deletePost(@PathParam("id") Long id, @Context SecurityContext ctx) {
        PostDTO post = postService.getPostById(id).orElseThrow();
        String email = jwt.getClaim("email");
        User user = userService.getUserByEmail(email).orElseThrow();
        if (ctx.isUserInRole("ROLE_ADMIN") || Objects.equals(post.getCreatedUserId(), user.getId())) {
            postService.deletePost(post.getId());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
