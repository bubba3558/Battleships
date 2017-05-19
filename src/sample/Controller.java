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
    @FXML private GridPane opponentGrid;
    private Cell [][]opponentBoard;
    private static String SHOOTEDCOLOR="#a19595";
    private static String HITFLOATINGCOLOR="##ff0303";
    private static String HITSUNKCCOLOR="#930505";


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        opponentBoard=new Cell[game.BOARDWIDTH][game.BOARDHIGHT];
      //  initMyBoard();
        initOpponentBoard();
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
    public void setShipHit(int x, int y ){
        opponentBoard[x][y].setShoot();
        opponentBoard[x][y].setStyle("-fx-background-color: " + HITFLOATINGCOLOR);
    }
    public  void setShipSunkHit(int x, int y){
        opponentBoard[x][y].setShoot();
        opponentBoard[x][y].setStyle("-fx-background-color: " + HITSUNKCCOLOR);
        //ToDO change other ship fields to sunkcolor
    }


    public void printMessage(String text){
        textField.setText(text);
    }
    public void initOpponentBoard(){
        for ( int x = 1; x<=game.BOARDWIDTH;++x){
            for ( int y = 1; y<=game.BOARDWIDTH;++y){
                Cell cell=new Cell (x,y);
                opponentGrid.add(cell,x,y);
                cell.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        printMessage("ataaaaak");
                    if (cell.wasShooted()==true){
                        printMessage("to polebylo juz przez ciebie wybrane");
                        return;
                    }
                    else {
                        cell.setShoot();
                        cell.setStyle("-fx-background-color: " + SHOOTEDCOLOR);
                        game.attackField(cell.x, cell.y);
                    }
                }
                });
            }
        }
    }


}
