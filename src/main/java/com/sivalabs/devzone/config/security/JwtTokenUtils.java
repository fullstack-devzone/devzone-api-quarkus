package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.users.entities.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import org.eclipse.microprofile.jwt.Claims;

@ApplicationScoped
public class JwtTokenUtils {

    public String generateToken(User user) {
        return Jwt.issuer("https://sivalabs.in/issuer")
                .subject(String.valueOf(user.getId()))
                .expiresAt(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC))
                .groups(Set.of(user.getRole().name()))
                .claim(Claims.email.name(), user.getEmail())
                .sign();
    }
}
