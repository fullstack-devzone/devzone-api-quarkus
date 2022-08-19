package com.sivalabs.devzone.users.repositories;

import com.sivalabs.devzone.users.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public User save(User userEntity) {
        persist(userEntity);
        return userEntity;
    }
}
