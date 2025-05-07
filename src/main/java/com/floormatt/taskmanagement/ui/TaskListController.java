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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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

        //display the current user
        displayCurrentUser();

        // Set up regular columns
        tc_taskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        tc_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up status filter dropdown with "All" option first
        ObservableList<String> statusOptions = FXCollections.observableArrayList("All");
        statusOptions.addAll(Arrays.stream(Task.TaskStatus.values())
                .map(Task.TaskStatus::toString)
                .collect(Collectors.toList()));

        cbx_statusFilter.setItems(statusOptions);
        cbx_statusFilter.getSelectionModel().selectFirst();

        //set up actions column
        tc_actions.setCellValueFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());
                    editTask(task);
                });

                deleteButton.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());
                    deleteTask(task);
                });
            }

            private void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        //load tasks for current user
        refreshTaskList();

    }

    private void displayCurrentUser() {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            lbl_helloUser.setText("Hello, " + currentUser.getUsername() + "!");
        } else {
            lbl_helloUser.setText("You are not logged in!");
            handleLogOut();
        }
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
            stage.setTitle("Task Management Application");
            authService.logout();
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @FXML
    private void showTaskForm() {
        try {
            Stage stage = (Stage) btn_addNewTask.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/taskForm.fxml"));

            loader.setControllerFactory(controllerFactory::create);

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Application");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @FXML
    private void refreshTaskList() {}
}
