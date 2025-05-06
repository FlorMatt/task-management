package com.floormatt.taskmanagement.ui;

import com.floormatt.taskmanagement.auth.AuthService;
import com.floormatt.taskmanagement.auth.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    //fxml injected components
    @FXML private TextField txtfld_lg_username;
    @FXML private PasswordField pwfld_lg_password;
    @FXML private Button btn_login;
    @FXML private Label lbl_loginFooter;

    //spring-managed dependencies
    private final AuthService authService;
    private final FxExceptionHandler exceptionHandler;
    private final ControllerFactory controllerFactory;

    @Autowired
    public LoginController(AuthService authService, FxExceptionHandler exceptionHandler, ControllerFactory controllerFactory) {
        this.authService = authService;
        this.exceptionHandler = exceptionHandler;
        this.controllerFactory = controllerFactory;
    }

    @FXML
    private void handleLogin() {
        User user = new User();
        user.setUsername(txtfld_lg_username.getText());
        user.setPassword(pwfld_lg_password.getText());

        if (txtfld_lg_username.getText().isEmpty() || pwfld_lg_password.getText().isEmpty()) {
            showAlert("Login Error", "The Username and Password fields cannot be empty.", Alert.AlertType.ERROR);
            return; //added to stop execution
        }

        try {
            if (authService.login(user)) {
                showAlert("Login Success", "Login was successful", Alert.AlertType.CONFIRMATION);

                //go to the task list to start working
                switchToTaskList();
            } else {
                showAlert("Login Error", "Invalid username or password", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @FXML
    private void switchToRegister() {
        try {
            Stage stage = (Stage) lbl_loginFooter.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/register.fxml"));

            //use the ControllerFactory for spring-aware controller creation
            loader.setControllerFactory(controllerFactory::create);

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Register");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    @FXML
    private void switchToTaskList() {
        try {
            Stage stage = (Stage) lbl_loginFooter.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/task-list.fxml"));

            //use the ControllerFactory for spring-aware controller creation
            loader.setControllerFactory(controllerFactory::create);

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
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
