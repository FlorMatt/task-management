package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.exception.FxExceptionHandler;
import com.floormatt.taskmanagement.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @FXML private TextField txtfld_lg_username;
    @FXML private PasswordField pwfld_lg_password;
    @FXML private Button btn_login;
    @FXML private Label lbl_loginFooter;

    //private final AuthService authService = new AuthService();

    private final AuthService authService;
    private final FxExceptionHandler exceptionHandler;

    @Autowired
    public LoginController(AuthService authService, FxExceptionHandler exceptionHandler) {
        this.authService = authService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    private void handleLogin() {
        User user = new User(txtfld_lg_username.getText(), pwfld_lg_password.getText());

        if (txtfld_lg_username.getText().isEmpty() || pwfld_lg_password.getText().isEmpty()) {
            showAlert("Login Error", "The Username and Password fields cannot be empty.", Alert.AlertType.ERROR);
            return; //added to stop execution
        }

        if (authService.login(user)) {
            showAlert("Login Successful", "Login was successful", Alert.AlertType.CONFIRMATION);
            //TODO: Switch to task list screen
        } else {
            System.out.println("Login Failed");
            showAlert("Login Failed", "Invalid username or password", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToRegister() {
        try {
            Stage stage = (Stage) lbl_loginFooter.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth/register.fxml"));
            loader.setControllerFactory(authService.getApplicationContext()::getBean);

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Register");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

}
