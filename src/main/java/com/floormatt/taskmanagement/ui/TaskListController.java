package com.floormatt.taskmanagement.ui;

import com.floormatt.taskmanagement.task.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @FXML
    public void initialize() {
        // Set up regular columns
        tc_taskName.setCellValueFactory(new PropertyValueFactory<>("title"));
        tc_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up status filter dropdown
        cbx_statusFilter.setItems(FXCollections.observableArrayList(
                "All", "TODO", "IN_PROGRESS", "COMPLETED"
        ));
        cbx_statusFilter.getSelectionModel().selectFirst();

    }

    private void editTask(Task task) {
    }

    private void deleteTask(Task task) {
    }

    @FXML
    private void handleLogOut() {}

    @FXML
    private void showTaskForm() {}

    @FXML
    private void refreshTaskList() {
    }
}
