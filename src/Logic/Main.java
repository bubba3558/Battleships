package Logic;

import graphic.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    private LoginController controller;
    static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
            this.stage=stage;
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphic/login.fxml"));
             Parent root = loader.load();
             controller = (LoginController) loader.getController();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             Platform.setImplicitExit(false);
             stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static Stage getStage(){
        return stage;
    }
}