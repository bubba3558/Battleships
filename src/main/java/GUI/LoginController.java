package GUI;

import Logic.Game;
import Network.NetworkManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Martyna on 23.05.2017.
 */
public class LoginController {
    private boolean wantToBeHost = true;
    private NetworkManager networkManager;
    private String IP;
    private int portNo;
    private Game game;
    private boolean tryingToConnect=false;

    @FXML private Label textWho;
    @FXML private Label textIP;
    @FXML private Label textPort;
    @FXML private TextField IPfield;
    @FXML private TextField portNoField;
    @FXML private Label errorText;
    @FXML private Label logText;

    public void changeHost(){
        if(tryingToConnect)
            return;
        if (wantToBeHost){
            wantToBeHost=false;
            textWho.setText("Chce dolaczyc do gry");
            textIP.setText("ID hosta: ");
            textPort.setText("numer portu hosta: ");
            logText.setText("");
        }
        else {
            wantToBeHost=true;
            textWho.setText("Chce stworzyc nowa gre");
            textIP.setText("Twoje IP:");
            textPort.setText("numer portu: ");
            logText.setText("Po kliknieciu polacz poczekaj na 2 gracza");
        }
    }
    public void getIP(){
        if( tryingToConnect)
            return;
        IP=IPfield.getText();
    }
    public void getPortNo(){
        if( tryingToConnect)
            return;
        portNo=Integer.parseInt(portNoField.getText());
    }
    public void printError(String text){
        errorText.setText(text);
        setTryingToConnectFalse();
    }
        public void connect()throws IOException{
            errorText.setText("");
            if( tryingToConnect)
                return;
            logText.setText("czekaj na 2 gracza");
            getIP();
            getPortNo();
            try {
                setTryingToConnectTrue();
                networkManager= new NetworkManager(wantToBeHost, portNo, IP, this);
                networkManager.run();
            }catch (Exception e){
                errorText.setText("Nie udalo sie utworzyc polaczeni :(  Czy na pewno podales wlasciwe numery? \n Moze port "+ portNo+ " jest zajety?");
                cancelConnection();
            }

        }

    public void cancelConnection(){
        if(networkManager!=null)
            networkManager.closeConnections();
        networkManager=null;
        logText.setText("");
        setTryingToConnectFalse();
    }

    public void startGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScene.fxml"));
        Parent root = loader.load();
        Controller gameController = (Controller) loader.getController();
        game = new Game(wantToBeHost, networkManager, gameController);
        gameController.setGame(game);
        networkManager.setGame(game);
        Stage stage= (Stage) errorText.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(e-> {
            networkManager.closeConnections();
            Platform.exit();
        });
    }
    private void setTryingToConnectFalse() {
        tryingToConnect = false;
        logText.setText("");
    }
    private void setTryingToConnectTrue(){
        logText.setText("Czekaj na drugiego gracza");
        errorText.setText("");
        tryingToConnect=true;
    }


}
