package Logic;

import GUI.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    private LoginController controller;
    @Override
    public void start(Stage stage) throws Exception {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
             Parent root = loader.load();
             controller = (LoginController) loader.getController();
             Scene scene = new Scene(root);
             Platform.setImplicitExit(false);
             stage.setScene(scene);
             stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}