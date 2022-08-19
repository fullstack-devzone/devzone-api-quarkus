package com.sivalabs.devzone.users.web.controller;

import com.sivalabs.devzone.users.entities.RoleEnum;
import com.sivalabs.devzone.users.models.CreateUserRequest;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        log.info("process=get_user, user_id={}", id);
        return userService
                .getUserById(id)
                .map(userDTO -> Response.ok(userDTO).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createUser(@Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email={}", createUserRequest.getEmail());
        UserDTO userDTO =
                new UserDTO(
                        null,
                        createUserRequest.getName(),
                        createUserRequest.getEmail(),
                        createUserRequest.getPassword(),
                        RoleEnum.ROLE_USER);
        UserDTO user = userService.createUser(userDTO);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }
}
