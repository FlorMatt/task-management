package com.floormatt.taskmanagement.ui;

import com.floormatt.taskmanagement.auth.AuthService;
import com.floormatt.taskmanagement.auth.User;
import com.floormatt.taskmanagement.task.Task;
import com.floormatt.taskmanagement.task.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskFormController {

    private Task taskToEdit;
    private boolean isEditMode = false;

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

        //set up the status combo box with enum values
        ObservableList<Task.TaskStatus> statusOptions = FXCollections.observableArrayList(Task.TaskStatus.values());
        cbx_status.setItems(statusOptions);

        //set default selection
        cbx_status.getSelectionModel().select(Task.TaskStatus.TODO);

        //set default due date to today for new tasks
        if (!isEditMode) {
            dtp_dueDate.setValue(LocalDate.now());
        }
    }

    @FXML
    private void saveTask() {
        try {
            // Validate inputs
            if (txtfld_taskName.getText().isEmpty()) {
                showAlert("Task Name Error", "Task name is required", Alert.AlertType.INFORMATION);
                return;
            }

            // Get current user
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                showAlert("Error", "User not logged in", Alert.AlertType.ERROR);
                return;
            }

            // Create or update task
            Task task = isEditMode ? taskToEdit : new Task();
            task.setTaskName(txtfld_taskName.getText());
            task.setCategory(txtfld_category.getText());
            task.setDueDate(dtp_dueDate.getValue());
            task.setStatus((Task.TaskStatus) cbx_status.getSelectionModel().getSelectedItem());

            if (isEditMode) {
                taskService.updateTask(task);
            } else {
                task.setUser(currentUser);
                taskService.createTask(task, currentUser);
            }

            // Return to task list
            switchToTaskList();

        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @FXML
    private void cancelTaskForm() {
        switchToTaskList();
        txtfld_taskName.clear();
        txtfld_category.clear();
        dtp_dueDate.setValue(null);
        cbx_status.getSelectionModel().clearSelection();
    }

    //set the task for editing
    public void setTaskToEdit(Task task) {
        this.taskToEdit = task;
        this.isEditMode = true;
        populateFormFields();
    }

    //populate the form fields
    private void populateFormFields() {
        if (taskToEdit != null) {
            txtfld_taskName.setText(taskToEdit.getTaskName());
            txtfld_category.setText(taskToEdit.getCategory());
            dtp_dueDate.setValue(taskToEdit.getDueDate());
            cbx_status.getSelectionModel().select(taskToEdit.getStatus());

        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToTaskList() {
        try {
            Stage stage = (Stage) txtfld_taskName.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/task-list.fxml"));

            //use the ControllerFactory for spring-aware controller creation
            loader.setControllerFactory(controllerFactory::create);

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Application");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }
}
