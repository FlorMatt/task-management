package com.floormatt.taskmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data //generates the getters, setters, toString, equals, hashCode
@Entity
@Table(name = "users")
@NoArgsConstructor //generates an empty constructor
@AllArgsConstructor //generates constructor with all fields
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
