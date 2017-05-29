package battleships.gui;

import battleships.model.Game;
import battleships.network.ErrorType;
import battleships.network.NetworkManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController implements LoggingInterface {
    private boolean wantToBeHost = true;
    private NetworkManager networkManager;
    private String ip;
    private int portNo;
    private Game game;
    private boolean tryingToConnect = false;

    @FXML
    private Label textWho;
    @FXML
    private Label textIp;
    @FXML
    private Label textPort;
    @FXML
    private TextField ipField;
    @FXML
    private TextField portNoField;
    @FXML
    private Label errorText;
    @FXML
    private Label logText;

    public void changeHost() {
        if (tryingToConnect) {
            errorText.setText("Trwa nawiązywanie połączenia.\n Jeżeli chcesz zmienić ustawienia, kliknij anuluj");
            return;
        }
        errorText.setText("");
        if (wantToBeHost) {
            wantToBeHost = false;
            textWho.setText("Chce dolaczyc do gry");
            textIp.setText("ID hosta: ");
            textPort.setText("numer portu hosta: ");
            logText.setText("");
        } else {
            wantToBeHost = true;
            textWho.setText("Chce stworzyc nowa gre");
            textIp.setText("Twoje IP:");
            textPort.setText("numer portu: ");
            logText.setText("Po kliknieciu połącz poczekaj na 2 gracza");
        }
    }

    public void getIp() {
        if (tryingToConnect)
            return;
        ip = ipField.getText();
    }

    public void getPortNo() {
        if (tryingToConnect)
            return;
        portNo = Integer.parseInt(portNoField.getText());
    }

    public void setError(ErrorType errorType) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                errorText.setText(getErrorMessage(errorType));
                setTryingToConnectFalse();
            }
        });
    }

    private String getErrorMessage(ErrorType errorType) {
        String text = "wystąpił błąd";
        switch (errorType) {
            case connectionTimeOut:
                text = "Nikt do Ciebie nie dołączył w przewidzianym czasie";
                break;
            case couldNotCreateStream:
                text = "Nie udało się utworzyć streamow dla wiadomosci";
                break;
        }
        return text;
    }

    public void connect() throws IOException {
        errorText.setText("");
        if (tryingToConnect) {
            errorText.setText("Trwa nawiązywanie połączenia.\n Jeżeli chcesz zmienić ustawienia, kliknij anuluj");
            return;
        }
        logText.setText("czekaj na 2 gracza");
        getIp();
        getPortNo();
        try {
            setTryingToConnectTrue();
            networkManager = new NetworkManager(wantToBeHost, portNo, ip, this);
            networkManager.run();
        } catch (Exception e) {
            errorText.setText("Nie udało się utworzyć polaczenia :(  Czy na pewno podałeś wlaściwe numery? Może port " + portNo + " jest zajęty?");
            cancelConnection();
        }

    }

    public void cancelConnection() {
        if (networkManager != null)
            networkManager.closeConnections();
        networkManager = null;
        logText.setText("");
        setTryingToConnectFalse();
    }

    /**
     * connected so change scene and allow to play
     */
    public void startGame() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/battleships/resources/fxml/gameScene.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    errorText.setText("Coś poszło nie taka podczas ładoania strony ");
                    setTryingToConnectFalse();
                }
                Controller gameController = (Controller) loader.getController();
                game = new Game(wantToBeHost, networkManager, gameController);
                gameController.setGame(game);
                networkManager.setGame(game);
                Stage stage = (Stage) errorText.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setOnCloseRequest(e -> {
                    networkManager.closeConnections();
                    Platform.exit();
                });
            }
        });
    }

    private void setTryingToConnectFalse() {
        tryingToConnect = false;
        logText.setText("");
    }

    private void setTryingToConnectTrue() {
        logText.setText("Czekaj na drugiego gracza");
        errorText.setText("");
        tryingToConnect = true;
    }


}