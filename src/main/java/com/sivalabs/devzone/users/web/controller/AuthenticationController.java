package com.sivalabs.devzone.users.web.controller;

import com.sivalabs.devzone.config.security.JwtTokenUtils;
import com.sivalabs.devzone.users.entities.RoleEnum;
import com.sivalabs.devzone.users.models.AuthUserDTO;
import com.sivalabs.devzone.users.models.AuthenticationRequest;
import com.sivalabs.devzone.users.models.AuthenticationResponse;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @POST
    @Path("/login")
    @PermitAll
    public Response createAuthenticationToken(@Valid AuthenticationRequest credentials) {
        Optional<UserDTO> userDTOOptional = userService.login(credentials.getUsername(), credentials.getPassword());
        if(userDTOOptional.isEmpty()){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserDTO user = userDTOOptional.orElseThrow();
        String token = jwtTokenUtils.generateToken(user);
        var response = new AuthenticationResponse(token, LocalDateTime.now().plusDays(7),
                new AuthUserDTO(user.getName(), user.getEmail(), user.getRole()));
        return Response.ok(response).build();
    }
}
