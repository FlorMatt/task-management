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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TaskListController {

    // FXML injected components
    @FXML private Label lbl_helloUser;
    @FXML private Button btn_logOut;
    @FXML private Button btn_addNewTask;
    @FXML private ComboBox<String> cbx_statusFilter;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> tc_taskName;
    @FXML private TableColumn<Task, String> tc_dueDate;
    @FXML private TableColumn<Task, String> tc_status;

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
        setupTableColumns();

        //set up the status filter
        setupStatusFilter();

        //set up table interactions
        setupTableInteractions();

        //load tasks for current user
        refreshTaskList();

    }

    private void setupTableColumns() {
        tc_taskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        tc_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupStatusFilter() {
        // Set up status filter dropdown with "All" option first
        ObservableList<String> statusOptions = FXCollections.observableArrayList("All");
        statusOptions.addAll(Arrays.stream(Task.TaskStatus.values())
                .map(Task.TaskStatus::toString)
                .toList());

        cbx_statusFilter.setItems(statusOptions);
        cbx_statusFilter.getSelectionModel().selectFirst();
    }

    private void setupTableInteractions() {
        //set up row double-click to edit
        taskTable.setRowFactory(taskTableView -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()){
                    editTask(row.getItem());
                }
            });
            return row;
        });

        //set up context menu for delete
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                deleteTask(selectedTask);
            }
        });
        contextMenu.getItems().add(deleteItem);
        taskTable.setContextMenu(contextMenu);
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

    private void editTask(Task task) {
        try {
            //load the task form fxml
            Stage stage = (Stage) taskTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task-form.fxml"));
            loader.setControllerFactory(controllerFactory::create);

            //get the controller and set the task to edit
            TaskFormController controller = loader.getController();
            controller.setTaskToEdit(task);

            //show the form
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Application");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    private void deleteTask(Task task) {
        //create confirmation dialog
        showAlert("Confirm Deletion?", "Are you sure you want to delete this task?" + task.getTaskName(), Alert.AlertType.CONFIRMATION);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Task: " + task.getTaskName());
        alert.setContentText("Are you sure you want to delete this task?");

        //show dialog and wait for response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                //delete task from database
                taskService.deleteTask(task.getId());

                //show success message
                showAlert("Success", "Task deleted successfully!", Alert.AlertType.INFORMATION);

                //refresh the task list
                refreshTaskList();
            }
            catch (Exception e) {
                exceptionHandler.handleException(e);
            }
        }
    }

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
    private void refreshTaskList() {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser != null) {
                //get selected status from filter
                String selectedStatus = cbx_statusFilter.getSelectionModel().getSelectedItem();

                //load tasks based on the filter
                List<Task> tasks;
                if ("All".equals(selectedStatus)) {
                    tasks = taskService.getAllTasksForUser(currentUser);
                } else {
                    Task.TaskStatus status = Task.TaskStatus.valueOf(selectedStatus.toUpperCase().replace(" ", "_"));
                    tasks = taskService.getTasksByStatus(currentUser, status);
                }

                //update the table with loaded tasks
                taskTable.setItems(FXCollections.observableArrayList(tasks));
            }
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
