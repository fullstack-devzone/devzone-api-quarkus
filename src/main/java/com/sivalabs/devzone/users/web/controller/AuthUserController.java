package com.sivalabs.devzone.users.web.controller;

import com.sivalabs.devzone.config.security.SecurityUtils;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final SecurityUtils securityUtils;

    @GET
    @Path("/auth/me")
    //@AnyAuthenticatedUser
    public Response me() {
        User loginUser = securityUtils.loginUser();
        if (loginUser != null) {
            AuthUserDTO userDTO =
                    AuthUserDTO.builder()
                            .name(loginUser.getName())
                            .email(loginUser.getEmail())
                            .role(loginUser.getRole())
                            .build();
            return Response.ok(userDTO).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
