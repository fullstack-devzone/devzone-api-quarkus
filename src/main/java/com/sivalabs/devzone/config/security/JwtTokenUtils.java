package com.sivalabs.devzone.config.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import com.sivalabs.devzone.users.models.UserDTO;
import org.eclipse.microprofile.jwt.Claims;

import io.smallrye.jwt.build.Jwt;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtTokenUtils {

    public String generateToken(UserDTO user) {
        return Jwt.issuer("https://sivalabs.in/issuer")
                .subject(String.valueOf(user.getId()))
                .expiresAt(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC))
                .groups(Set.of(user.getRole().name()))
                .claim(Claims.email.name(), user.getEmail())
                .sign();
    }
}
