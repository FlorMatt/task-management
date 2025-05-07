package com.floormatt.taskmanagement.ui;

import com.floormatt.taskmanagement.auth.AuthService;
import com.floormatt.taskmanagement.auth.User;
import com.floormatt.taskmanagement.task.Task;
import com.floormatt.taskmanagement.task.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class TaskFormController {

    // FXML injected components
    @FXML private TextField txtfld_taskName;
    @FXML private TextField txtfld_category;
    @FXML private DatePicker dtp_dueDate;
    @FXML private ComboBox cbx_status;

    // Spring-managed dependencies
    private final TaskService taskService;
    private final AuthService authService;
    private final FxExceptionHandler exceptionHandler;
    private final ControllerFactory controllerFactory;

    @Autowired
    public TaskFormController(TaskService taskService, AuthService authService, FxExceptionHandler exceptionHandler, ControllerFactory controllerFactory) {
        this.taskService = taskService;
        this.authService = authService;
        this.exceptionHandler = exceptionHandler;
        this.controllerFactory = controllerFactory;
    }

    @FXML
    private void initialize() {

        //if we clicked edit task, this will need to load from the database
        // Set up status filter dropdown with "To Do" option first
        ObservableList<String> statusOptions = FXCollections.observableArrayList("To Do");
        statusOptions.addAll(Arrays.stream(Task.TaskStatus.values())
                .map(Task.TaskStatus::toString)
                .collect(Collectors.toList()));

        cbx_status.setItems(statusOptions);
        cbx_status.getSelectionModel().selectFirst();
    }

    @FXML
    private void saveTask() {}

    @FXML
    private void cancelTaskForm() {}
}
