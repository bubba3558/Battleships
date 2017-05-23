package sample;

import Logic.Game;
import Network.NetworkManager;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Created by Martyna on 23.05.2017.
 */
public class LoginController {
    private boolean wantToBeHost = true;

    @FXML private Label textWho;
    @FXML private Label textIP;
    @FXML private Label textPortNo;
    @FXML private TextField IPfield;
    @FXML private TextField portNoField;
    @FXML private Label Errortext;
    private String IP;
    private int portNo;
    private Game game;
    public void changeHost(){
        if (wantToBeHost){
            wantToBeHost=false;
            textWho.setText("Chce dolaczyc do gry");
            textIP.setText("ID hosta: ");
            textPortNo.setText("numer portu hosta: ");
        }
        else {
            wantToBeHost=true;
            textWho.setText("Chce stworzyc nowa gre");
            textIP.setText("Twoje IP:");
            textPortNo.setText("numer portu: ");
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
          NetworkManager networkManager= new NetworkManager(wantToBeHost, portNo, IP);
          if(networkManager.initConnection())
              startGame();
          else{


          }

    }
    public void startGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sample.fxml"));
        Parent root = loader.load();
        game.setController( (Controller) loader.getController() );
    }


}
