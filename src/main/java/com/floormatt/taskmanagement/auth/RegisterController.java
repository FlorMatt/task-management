package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.exception.FxExceptionHandler;
import com.floormatt.taskmanagement.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterController {

    @FXML private TextField txtfld_reg_username;
    @FXML private PasswordField pwfld_reg_password;
    @FXML private Button btn_register;
    @FXML private Label lbl_registerFooter;

    //private final AuthService authService = new AuthService();

    private final AuthService authService;
    private final FxExceptionHandler exceptionHandler;

    @Autowired
    public RegisterController(AuthService authService, FxExceptionHandler exceptionHandler) {
        this.authService = authService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    private void handleRegister() {
        User newUser = new User(txtfld_reg_username.getText(), pwfld_reg_password.getText());

        if (txtfld_reg_username.getText().isEmpty() || pwfld_reg_password.getText().isEmpty()) {
            showAlert("Register Error", "The Username and Password fields cannot be empty.", Alert.AlertType.ERROR);
            return; //added to stop execution
        }

        try {
            if (authService.register(newUser)) {
                showAlert("Registration Successful", "Account created successfully", Alert.AlertType.CONFIRMATION);
                switchToLogin(); //go to log in after registering
            } else {
                showAlert("Registration Failed", "Username already exists", Alert.AlertType.ERROR);
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

    @FXML
    private void switchToLogin() {
        try {
            Stage stage = (Stage) lbl_registerFooter.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth/login.fxml"));
            loader.setControllerFactory(authService.getApplicationContext()::getBean);

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Login");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }
}
