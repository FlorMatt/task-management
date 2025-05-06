package com.floormatt.taskmanagement.ui;

import com.floormatt.taskmanagement.task.Task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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

        // Set up Actions column with buttons
        setupActionColumn();

        refreshTaskList();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Task, Void>, TableCell<Task, Void>> cellFactory =
                new Callback<>() {
                    @Override
                    public TableCell<Task, Void> call(final TableColumn<Task, Void> param) {
                        return new TableCell<>() {
                            private final Button editBtn = new Button("Edit");
                            private final Button deleteBtn = new Button("Delete");

                            {
                                // Style buttons
                                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                                // Set button actions
                                editBtn.setOnAction(event -> {
                                    Task task = getTableView().getItems().get(getIndex());
                                    editTask(task);
                                });

                                deleteBtn.setOnAction(event -> {
                                    Task task = getTableView().getItems().get(getIndex());
                                    deleteTask(task);
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                                    setGraphic(buttons);
                                }
                            }
                        };
                    }
                };

        tc_actions.setCellFactory(cellFactory);
    }

    private void editTask(Task task) {
        try {
            // Load the task form
            Stage stage = (Stage) taskTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/task-form.fxml"));
            loader.setControllerFactory(ControllerFactory::create);
            stage.setScene(new Scene(loader.load()));

            // Get controller and pass the task to edit
            TaskFormController controller = loader.getController();
            controller.setTaskToEdit(task);

            stage.setTitle("Edit Task");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: Add proper error handling
        }
    }

    private void deleteTask(Task task) {
        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Task");
        alert.setContentText("Are you sure you want to delete: " + task.getTitle() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO: Implement actual deletion through TaskService
                refreshTaskList();
                System.out.println("Deleted task: " + task.getTitle());
            }
        });
    }

    @FXML
    private void handleLogOut() {}

    @FXML
    private void showTaskForm() {}

    @FXML
    private void refreshTaskList() {}
}
