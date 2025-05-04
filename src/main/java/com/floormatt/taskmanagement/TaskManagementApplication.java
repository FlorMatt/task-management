package com.floormatt.taskmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagementApplication extends Application {

    public static void main(String[] args) {
        //SpringApplication.run(TaskManagementApplication.class, args);

        //start JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //load the inital FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/floormatt/taskmanagement/auth/login.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Task Management Login");
        primaryStage.show();
    }

}
