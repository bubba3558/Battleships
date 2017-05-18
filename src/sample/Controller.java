package sample;
import Logic.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;;
import javafx.scene.*;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    Game game;

    @FXML private TextField textField;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    private void bottomHandler(ActionEvent event)
    {
        if (game.getStage()==GameStage.START);
            //TO DO check if conf is correct
        else if(game.getStage()==GameStage.PLAYING){
            if( ! game.isYourTurn() ){
                textField.setText("Poczekaj na ruch przeciwnika");
            }
            else
                textField.setText("Aby zaatakowac nacisnij wybrane pole na tablicy przeciwnika");
         }
    }
    public void setMiss(int x, int y ){

    }
    public void setHit(int x, int y ){

    }


    public void printMessage(String text){
        textField.setText(text);
    }
}
