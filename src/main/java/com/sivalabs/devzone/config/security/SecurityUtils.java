package com.sivalabs.devzone.config.security;

import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.services.UserService;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserService userService;

    public User loginUser() {
        return userService.getUserByEmail("admin@gmail.com").orElseThrow();
    }
}
