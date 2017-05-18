package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Controller controller;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML.fxml"));
        Parent root = loader.load();
        controller = (Controller)loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public Controller getController(){
        return controller;
    }
    public static void main(String[] args) {
        launch(args);
    }
}