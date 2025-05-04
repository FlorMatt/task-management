package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtfld_lg_username;
    @FXML private PasswordField pwfld_lg_password;
    @FXML private Button btn_login;
    @FXML private Label lbl_loginFooter;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        User user = new User(txtfld_lg_username.getText(), pwfld_lg_password.getText());

        if (txtfld_lg_username.getText().isEmpty() || pwfld_lg_password.getText().isEmpty()) {
            showAlert("Login Error", "The Username and Password fields cannot be empty.");
        }

        if (authService.login(user)) {
            System.out.println("Login Successful");
            //TODO: Switch to task list screen
        } else {
            System.out.println("Login Failed");
            showAlert("Login Failed", "Invalid username or password");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToRegister() {
        try {
            //get the current stage
            Stage stage = (Stage) lbl_loginFooter.getScene().getWindow();

            //load register.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/floormatt/taskmanagement/auth/register.fxml"));
            Parent root = loader.load();

            //switch scene
            stage.setScene(new Scene(root));
            stage.setTitle("Task Management Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
