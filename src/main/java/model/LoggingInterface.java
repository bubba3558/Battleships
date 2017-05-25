package model;

import gui.Controller;
import gui.LoginController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.NetworkManager;

import java.io.IOException;

public class LoggingInterface {

    LoginController loginController;
    NetworkManager networkManager;
    Stage stage;
    Game game;
    boolean isHost;

//    public LoggingInterface(LoginController loginController) {
//        this.loginController = loginController;
//    }

    public LoggingInterface(Stage stage) {
        this.stage = stage;
        try {
            initLogGui();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean tryToConnect(boolean isHost, int port, String serverIp) {
        this.isHost = isHost;
        try {
            networkManager = new NetworkManager(isHost, port, serverIp, this);
            networkManager.run();
        } catch (Exception e) {
            loginController.printError("Nie udało się utworzyć polaczenia. Czy na pewno podałeś wlaściwe numery? Może port " + port + " jest zajęty?");
            cancelConnection();
            return false;
        }
        return true;
    }

    public void setError(String text) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loginController.printError(text);
            }
        });
    }

    public void cancelConnection() {
        if (networkManager != null) {
            networkManager.closeConnections();
        }
        networkManager = null;
        loginController.clearAfterConnectionTry();
    }

    public void startGame() {
        /**connected so change scene and allow to play*/
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    initGame();
                } catch (IOException e) {
                    loginController.printError("oops cos poszło nie tak przy ładowaniu widoku gry");
                    e.printStackTrace();
                }
            }
        });
    }

    private void initGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameScene.fxml"));
        Parent root = loader.load();
        Controller gameController = loader.getController();
        game = new Game(isHost, networkManager, gameController);
        gameController.setGame(game);
        networkManager.setGame(game);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            networkManager.closeConnections();
            Platform.exit();
        });
        loginController = null;
    }

    private void initLogGui() throws IOException {

        stage.setOnCloseRequest(e -> {
            cancelConnection();
            Platform.exit();
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        loginController = loader.getController();
        loginController.setLoggingInterface(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e ->
        {
            cancelConnection();
            Platform.exit();
        });
    }
}



