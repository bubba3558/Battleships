package sample;

import Logic.Game;
import Network.NetworkManager;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Martyna on 23.05.2017.
 */
public class LoginController {
    private boolean wantToBeHost = true;
    NetworkManager networkManager;
    private String IP;
    private int portNo;
    private Game game;

    @FXML private Label textWho;
    @FXML private Label textIP;
    @FXML private Label textPort;
    @FXML private TextField IPfield;
    @FXML private TextField portNoField;
    @FXML private Label errorText;

    public void changeHost(){
        if (wantToBeHost){
            wantToBeHost=false;
            textWho.setText("Chce dolaczyc do gry");
            textIP.setText("ID hosta: ");
            textPort.setText("numer portu hosta: ");
        }
        else {
            wantToBeHost=true;
            textWho.setText("Chce stworzyc nowa gre");
            textIP.setText("Twoje IP:");
            textPort.setText("numer portu: ");
        }
    }
    public void getIP(){
        IP=IPfield.getText();
        System.out.println(IP);
    }
    public void getPortNo(){
        portNo=Integer.parseInt(portNoField.getText());
        System.out.println(portNo);
    }
        public void connect()throws IOException{
            getIP();
            getPortNo();
            networkManager= new NetworkManager(wantToBeHost, portNo, IP);
            if(networkManager.initConnection())
                startGame();
            else{
                errorText.setText("Nie udalo sie utworzyc polaczeni :(  Sprobuj ponownie");
                networkManager=null;
            }

        }
    public void startGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sample.fxml"));
//        Stage oldStage = (Stage) IPfield.getScene().getWindow();
//        oldStage.close();
        Parent root = loader.load();
        Controller controller = (Controller) loader.getController();
        game = new Game(wantToBeHost, networkManager, controller);
        controller.setGame(game);
        networkManager.setGame(game);
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
        Platform.setImplicitExit(false);
        //Main.stage.show();
    }


}
