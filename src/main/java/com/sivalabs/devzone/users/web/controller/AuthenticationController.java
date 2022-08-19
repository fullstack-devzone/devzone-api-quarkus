package com.sivalabs.devzone.users.web.controller;

import com.sivalabs.devzone.users.entities.RoleEnum;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import com.sivalabs.devzone.users.models.AuthenticationRequest;
import com.sivalabs.devzone.users.models.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    @POST
    @Path("/login")
    public Response createAuthenticationToken(@Valid AuthenticationRequest credentials) {
        var response = new AuthenticationResponse("dummy-token", LocalDateTime.now().plusDays(7),
                new AuthUserDTO("Admin", "admin@gmail.com", RoleEnum.ROLE_ADMIN));
        return Response.ok(response).build();
    }
}
