package com.floormatt.taskmanagement.task;

import com.floormatt.taskmanagement.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    //srping-managed dependencies
    private final TaskRepository taskRepository;

    @Transactional
    public Task createTask(Task task, User user) {
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasksForUser(User user) {
        return taskRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(User user, Task.TaskStatus status) {
        return taskRepository.findByUserAndStatus(user, status);
    }

    @Transactional
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

}
