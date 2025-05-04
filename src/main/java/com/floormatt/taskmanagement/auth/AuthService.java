package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    // Thread-safe user storage
    private final Map<String, User> registeredUsers = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    // Initialization block (runs after constructor)
    {
        registerTestUser();
    }

    private void registerTestUser() {
        User testUser = new User(
                "testUser",
                passwordEncoder.encode("testPassword123")
        );

        registeredUsers.put(testUser.getUsername(), testUser);
        log.info("Test user registered: {}", testUser.getUsername());
    }

    public boolean login(User user) {
        User storedUser = registeredUsers.get(user.getUsername());
        return storedUser != null &&
                passwordEncoder.matches(user.getPassword(), storedUser.getPassword());
    }

    public boolean register(User newUser) {
        if (isInvalidUser(newUser)) {
            log.warn("Registration failed - invalid user data");
            return false;
        }

        if (registeredUsers.containsKey(newUser.getUsername())) {
            log.warn("Registration failed - username exists: {}", newUser.getUsername());
            return false;
        }

        User userToRegister = new User(
                newUser.getUsername(),
                passwordEncoder.encode(newUser.getPassword())
        );

        registeredUsers.put(userToRegister.getUsername(), userToRegister);
        log.info("New user registered: {}", userToRegister.getUsername());
        return true;
    }

    private boolean isInvalidUser(User user) {
        return user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty();
    }
}
