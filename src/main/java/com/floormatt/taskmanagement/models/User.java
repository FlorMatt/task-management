package com.floormatt.taskmanagement.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data //generates the getters, setters, toString, equals, hashCode
@NoArgsConstructor //generates an empty constructor
@AllArgsConstructor //generates constructor with all fields
public class User {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
