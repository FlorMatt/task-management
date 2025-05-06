package com.floormatt.taskmanagement.auth;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    //get currently authenticated user
    @Getter
    private User currentUser;

    //srping-managed dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //create a default test user
    @PostConstruct
    @Transactional
    public void registerTestUser() {
        if (userRepository.findByUsername("testUser").isEmpty()) {
            User testUser = new User();
            testUser.setUsername("testUser");
            testUser.setPassword(passwordEncoder.encode("testPassword"));
            userRepository.save(testUser);
            log.info("Registered test user: " + testUser.getUsername());
        }
    }

    //attempt to log in by matching a raw password against the stored hash
    @Transactional(readOnly = true)
    public boolean login(User user) {
        boolean success = userRepository.findByUsername(user.getUsername()).map(storedUser -> passwordEncoder.matches(user.getPassword(), storedUser.getPassword())).orElse(false);

        if (success) {
            this.currentUser = userRepository.findByUsername(user.getUsername()).get();
            log.info("User Logged in: " + user.getUsername());
        }
        return success;
    }

    //log out current user
    public void logout() {
        if (currentUser != null) {
            log.info("User logged out: {}", currentUser.getUsername());
            this.currentUser = null;
        }
    }

    //register a new user if valid and username is not already taken
    @Transactional
    public boolean register(User newUser) {
        if (isInvalidUser(newUser)) {
            log.error("Registration failed - Please provide a username and password");
            return false;
        }

        if (userRepository.existsByUsername(newUser.getUsername())) {
            log.error("Registration failed - Username already exists");
            return false;
        }

        User userToRegister = new User();
        userToRegister.setUsername(newUser.getUsername());
        userToRegister.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(userToRegister);

        log.info("Registered new user: " + newUser.getUsername());
        return true;

    }

    //check if username/password presence
    private boolean isInvalidUser(User user) {
        return user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty();
    }
}
