import Logic.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controller;

public class Main extends Application {
    Controller controller;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sample.fxml"));
        Parent root = loader.load();
        controller = (Controller)loader.getController();
        Game game= new Game(false,4444,"127.0.0.1", controller);
        controller.setGame(game);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Platform.setImplicitExit(false);
        stage.show();
    }
    public Controller getController(){
        return controller;
    }
    public static void main(String[] args) {
        launch(args);
    }
}