package com.floormatt.taskmanagement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TaskManagementApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent root;

    public static void main(String[] args) {
        //start JavaFX
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder().sources(TaskManagementApplication.class).run();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login.fxml"));
        loader.setControllerFactory(springContext::getBean);
        root = loader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Task Management Application");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
        Platform.exit();
    }

}
