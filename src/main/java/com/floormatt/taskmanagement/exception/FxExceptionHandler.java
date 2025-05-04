package com.floormatt.taskmanagement.exception;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Component;

@Component
public class FxExceptionHandler {

    public void handleException(Throwable throwable) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText("An unexpected error occurred");
        alert.setContentText(throwable.getMessage());
        alert.showAndWait();
    }
}
