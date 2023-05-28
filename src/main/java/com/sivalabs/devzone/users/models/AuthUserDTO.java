package com.sivalabs.devzone.users.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sivalabs.devzone.users.entities.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private RoleEnum role;
}
