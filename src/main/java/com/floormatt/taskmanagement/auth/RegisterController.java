package com.floormatt.taskmanagement.auth;

import com.floormatt.taskmanagement.exception.FxExceptionHandler;
import com.floormatt.taskmanagement.models.User;
import com.floormatt.taskmanagement.util.ControllerFactory;
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

    //fxml injected components
    @FXML private TextField txtfld_reg_username;
    @FXML private PasswordField pwfld_reg_password;
    @FXML private Button btn_register;
    @FXML private Label lbl_registerFooter;

    //spring-managed dependencies
    private final AuthService authService;
    private final FxExceptionHandler exceptionHandler;
    private final ControllerFactory controllerFactory;

    @Autowired
    public RegisterController(AuthService authService, FxExceptionHandler exceptionHandler, ControllerFactory controllerFactory) {
        this.authService = authService;
        this.exceptionHandler = exceptionHandler;
        this.controllerFactory = controllerFactory;
    }

    @FXML
    private void handleRegister() {
        User newUser = new User();
        newUser.setUsername(txtfld_reg_username.getText());
        newUser.setPassword(pwfld_reg_password.getText());

        if (txtfld_reg_username.getText().isEmpty() || pwfld_reg_password.getText().isEmpty()) {
            showAlert("Registration Error", "The Username and Password fields cannot be empty.", Alert.AlertType.ERROR);
            return; //added to stop execution
        }

        try {
            if (authService.register(newUser)) {
                showAlert("Registration Success", "Account created successfully", Alert.AlertType.CONFIRMATION);

                //go to log in after registering
                switchToLogin();
            } else {
                showAlert("Registration Error", "Username already exists", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }

    }

    @FXML
    private void switchToLogin() {
        try {
            Stage stage = (Stage) lbl_registerFooter.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth/login.fxml"));

            //use ControllerFactory for spring-aware controller creation
            loader.setControllerFactory(controllerFactory::create);

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Task Management Login");
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
