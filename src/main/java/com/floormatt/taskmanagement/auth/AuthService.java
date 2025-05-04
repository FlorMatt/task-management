package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.models.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    //Temp mock storage - replace later with database
    private final User mockUser = new User("testUser", "testPassword123");


    //Mock login - validates against hardcoded user
    public boolean login(User user) {
        return user.getUsername().equals(mockUser.getUsername()) && user.getPassword().equals(mockUser.getPassword());
    }

    //Mock registration - only logs the user for now
    public boolean register(User newUser) {
        System.out.println("Registering user: " + newUser.getUsername());
        return true; //always succeeds in mock
    }
}
