package com.sivalabs.devzone.users.services;

import io.quarkus.elytron.security.common.BcryptUtil;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordEncoder {
    public String encode(String password) {
        return BcryptUtil.bcryptHash(password);
    }

    public boolean matching(String plainText, String encryptedText) {
        return BcryptUtil.matches(plainText, encryptedText);
    }
}
