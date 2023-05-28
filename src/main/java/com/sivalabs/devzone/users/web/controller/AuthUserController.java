package com.sivalabs.devzone.users.web.controller;

import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import com.sivalabs.devzone.users.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final UserService userService;
    private final JsonWebToken jwt;

    @GET
    @Path("/me")
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public Response me() {
        String email = jwt.getClaim("email");
        User loginUser = userService.getUserByEmail(email).orElse(null);
        if (loginUser != null) {
            AuthUserDTO userDTO = new AuthUserDTO(loginUser.getName(), loginUser.getEmail(), loginUser.getRole());
            return Response.ok(userDTO).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
