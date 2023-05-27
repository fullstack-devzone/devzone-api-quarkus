package com.sivalabs.devzone.users.services;

import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findByIdOptional(id).map(UserDTO::fromEntity);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DevZoneException("Email " + user.getEmail() + " is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = user.toEntity();
        userEntity.setRole(user.getRole());
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public Optional<UserDTO> login(String email, String password) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            return Optional.empty();
        }
        User user = byEmail.orElseThrow();
        if (passwordEncoder.matching(password, user.getPassword())) {
            var userDTO = UserDTO.fromEntity(user);
            userDTO.setPassword(null);
            return Optional.of(userDTO);
        }
        return Optional.empty();
    }
}
