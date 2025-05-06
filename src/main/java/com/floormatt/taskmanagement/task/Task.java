package com.floormatt.taskmanagement.task;

import com.floormatt.taskmanagement.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //task name
    @Column(nullable = false)
    private String taskName;

    //due date
    @Column(name = "due_date")
    private LocalDate dueDate;

    //category
    private String category;

    //status
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    //link to user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum TaskStatus {
        ALL, TODO, In_PROGRESS, COMPLETED
    }
}
