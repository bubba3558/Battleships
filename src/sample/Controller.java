package sample;
import Logic.*;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    Game game;

    @FXML private TextArea textField;
    @FXML private GridPane myBoard;
    @FXML private GridPane opponentBoard;
    private static String SHOTEDCOLOR="#a19595";


    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //  initMyBoard();
        //initOpponentBoard();
    }
    public void bottomHandler(ActionEvent event)
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
    public void setGame(Game game){
        this.game=game;
    }
    public void setMiss(int x, int y ){

    }
    public void setHit(int x, int y ){

    }


    public void printMessage(String text){
        textField.setText(text);
    }
    public void initOpponentBoard(){
        for ( int x = 1; x<=game.BOARDWIDTH;++x){
            for ( int y = 1; y<=game.BOARDWIDTH;++y){
                Cell cell=new Cell (x,y);
                cell.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                    if (cell.wasShoted=true){
                        printMessage("to polebylo juz przez ciebie wybrane");
                        return;
                    }
                    else{
                            cell.wasShoted=true;
                            cell.setStyle("-fx-background-color: "+ SHOTEDCOLOR);
                            game.attackField(cell.x, cell.y);
                    }
                }
                });;
            }
        }
    }


}
