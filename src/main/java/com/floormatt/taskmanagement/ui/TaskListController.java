package com.floormatt.taskmanagement.ui;

import com.floormatt.taskmanagement.auth.AuthService;
import com.floormatt.taskmanagement.task.Task;
import com.floormatt.taskmanagement.task.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class TaskListController {

    // FXML injected components
    @FXML private Label lbl_helloUser;
    @FXML private Button btn_logOut;
    @FXML private Button btn_addNewTask;
    @FXML private ComboBox<String> cbx_statusFilter;
    @FXML private Button btn_refreshList;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> tc_taskName;
    @FXML private TableColumn<Task, String> tc_dueDate;
    @FXML private TableColumn<Task, String> tc_status;
    @FXML private TableColumn<Task, Void> tc_actions;

    // Spring-managed dependencies
    private final TaskService taskService;
    private final AuthService authService;
    private final FxExceptionHandler exceptionHandler;
    private final ControllerFactory controllerFactory;

    @Autowired
    public TaskListController(TaskService taskService, AuthService authService, FxExceptionHandler exceptionHandler, ControllerFactory controllerFactory) {
        this.taskService = taskService;
        this.authService = authService;
        this.exceptionHandler = exceptionHandler;
        this.controllerFactory = controllerFactory;
    }


    @FXML
    public void initialize() {
        // Set up regular columns
        tc_taskName.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up status filter dropdown with "All" option first
        ObservableList<String> statusOptions = FXCollections.observableArrayList("All");
        statusOptions.addAll(Arrays.stream(Task.TaskStatus.values())
                .map(Task.TaskStatus::toString)
                .collect(Collectors.toList()));

        cbx_statusFilter.setItems(statusOptions);
        cbx_statusFilter.getSelectionModel().selectFirst();

    }

    private void editTask(Task task) {}

    private void deleteTask(Task task) {}

    @FXML
    private void handleLogOut() {
        try {
            Stage stage = (Stage) btn_logOut.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login.fxml"));
            loader.setControllerFactory(controllerFactory::create);
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Login");
            authService.logout();
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @FXML
    private void showTaskForm() {}

    @FXML
    private void refreshTaskList() {}
}
