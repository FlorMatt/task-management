package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.models.User;
import jakarta.annotation.PostConstruct;
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

    //srping-managed dependencies
    private final PasswordEncoder passwordEncoder;

    //create a default test user
    @PostConstruct
    public void registerTestUser() {

        if (!registeredUsers.containsKey("testUser")) {
            User testUser = new User("testUser", passwordEncoder.encode("testPassword123"));
            registeredUsers.put(testUser.getUsername(), testUser);
            log.info("Test user registered: {}", testUser.getUsername());
        }
    }

    //attempt to log in by matching a raw password against the stored hash
    public boolean login(User user) {
        User storedUser = registeredUsers.get(user.getUsername());
        return storedUser != null &&
                passwordEncoder.matches(user.getPassword(), storedUser.getPassword());
    }

    //register a new user if valid and username is not already taken
    public boolean register(User newUser) {
        if (isInvalidUser(newUser)) {
            log.warn("Registration failed - invalid user data");
            return false;
        }

        if (registeredUsers.containsKey(newUser.getUsername())) {
            log.warn("Registration failed - username exists: {}", newUser.getUsername());
            return false;
        }

        User userToRegister = new User(newUser.getUsername(), passwordEncoder.encode(newUser.getPassword()));
        registeredUsers.put(userToRegister.getUsername(), userToRegister);

        log.info("New user registered: {}", userToRegister.getUsername());
        return true;
    }

    //check if username/password presence
    private boolean isInvalidUser(User user) {
        return user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty();
    }
}
