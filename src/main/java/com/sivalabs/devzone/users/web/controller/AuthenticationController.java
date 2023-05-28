package com.sivalabs.devzone.users.web.controller;

import com.sivalabs.devzone.config.security.JwtTokenUtils;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import com.sivalabs.devzone.users.models.AuthenticationRequest;
import com.sivalabs.devzone.users.models.AuthenticationResponse;
import com.sivalabs.devzone.users.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @POST
    @Path("/login")
    @PermitAll
    public Response createAuthenticationToken(@Valid AuthenticationRequest credentials) {
        Optional<User> userDTOOptional = userService.login(credentials.getUsername(), credentials.getPassword());
        if (userDTOOptional.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User user = userDTOOptional.orElseThrow();
        String token = jwtTokenUtils.generateToken(user);
        var response = new AuthenticationResponse(
                token,
                LocalDateTime.now().plusDays(7),
                new AuthUserDTO(user.getName(), user.getEmail(), user.getRole()));
        return Response.ok(response).build();
    }
}
