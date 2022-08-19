package com.sivalabs.devzone.users.services;

import com.sivalabs.devzone.users.entities.RoleEnum;
import com.sivalabs.devzone.users.entities.User;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@RequiredArgsConstructor
public class SecurityService {
    private final UserService userService;

    public User loginUser() {
        return userService.getUserByEmail("admin@gmail.com").orElseThrow();
    }

    public boolean isCurrentUserAdmin() {
        return isUserHasAnyRole(loginUser(), RoleEnum.ROLE_ADMIN);
    }

    private boolean isUserHasAnyRole(User loginUser, RoleEnum... roles) {
        return Arrays.asList(roles).contains(loginUser.getRole());
    }
}
