package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField txtfld_reg_username;
    @FXML private PasswordField pwfld_reg_password;
    @FXML private Button btn_register;
    @FXML private Label lbl_registerFooter;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleRegister() {
        User newUser = new User(txtfld_reg_username.getText(), pwfld_reg_password.getText());

        if (txtfld_reg_username.getText().isEmpty() || pwfld_reg_password.getText().isEmpty()) {
            showAlert("Register Error", "The Username and Password fields cannot be empty.");
        }

        if (authService.register(newUser)) {
            showAlert("Registration Successful", "Account created successfully");
            switchToLogin(); //go to log in after registering
        } else {
            showAlert("Registration Failed", "Username already exists");
        }
    }

    @FXML
    private void switchToLogin() {
        try {
            //get current stage
            Stage stage = (Stage) lbl_registerFooter.getScene().getWindow();

            //load login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/floormatt/taskmanagement/auth/login.fxml"));
            Parent root = loader.load();

            //switch scene
            stage.setScene(new Scene(root));
            stage.setTitle("Task Management Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
