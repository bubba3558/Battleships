package model;

import gui.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    LoggingInterface loggingInterface;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Statki");
        loggingInterface = new LoggingInterface(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
