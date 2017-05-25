package gui;

import javafx.fxml.Initializable;
import model.Game;
import model.LoggingInterface;
import network.NetworkManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {
    private boolean wantToBeHost = true;
    private String ip;
    private int portNo;
    private boolean tryingToConnect = false;
    private LoggingInterface loggingInterface;
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
        if (tryingToConnect)
            return;
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
            logText.setText("Po kliknieciu polacz poczekaj na 2 gracza");
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

    public void setLoggingInterface(LoggingInterface loggingInterface) {
        this.loggingInterface = loggingInterface;
    }
    public void cancelConnection() {
        loggingInterface.cancelConnection();
    }

    public void printError(String text) {
        errorText.setText(text);
        setTryingToConnectFalse();
    }

    public void connect() throws IOException {
        errorText.setText("");
        if (tryingToConnect) {
            errorText.setText("Próbujesz się już łączyć");
            return;
        }
        logText.setText("czekaj na 2 gracza");
        getIp();
        getPortNo();
        setTryingToConnectTrue();
        loggingInterface.tryToConnect(wantToBeHost, portNo, ip);

    }

    public void clearAfterConnectionTry() {
        logText.setText("");
        setTryingToConnectFalse();
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
