package sample;
import Logic.*;

import javafx.application.Platform;
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
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    Game game;
    Boolean alreadyStarted=false;

    @FXML private TextArea textField;
    @FXML private GridPane myGrid;
    @FXML private GridPane opponentGrid;
    @FXML private Checkbox checkbox;
    private Cell [][]opponentBoard;
    private Cell [][]myBoard;
    public final static String HITFLOATINGCOLOR="#ff0303";
    public final static String HITSUNKCOLOR="#930505";
    public final static String MISSCOLOR="#58648c";
    public final static String SHIPCOLOR="#428908";



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        opponentBoard=new Cell[game.BOARDWIDTH+1][game.BOARDHIGHT+1];
        myBoard=new Cell[game.BOARDWIDTH+1][game.BOARDHIGHT+1];
        initMyBoard();
        initOpponentBoard();
        Platform.setImplicitExit(false);
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
        if(game.isYourTurn())
            printMessage("rozpocznij gre");
        else
            printMessage("poczekaj na ruch przeciwnika");
    }
    public void setMiss(int x, int y ){
        opponentBoard[x][y].setStyle("-fx-background-color: "+MISSCOLOR);//getChildren().add(new Rectangle(15,15));
        printMessage("Pudlo, kolej przeciwnika");
    }
    public void setOpponentMiss(int x, int y ){
        myBoard[x][y].setShoot();
        myBoard[x][y].setStyle("-fx-background-color: "+MISSCOLOR);
        printMessage("Przeciwnik spudlowal. Twoja kolej");
    }

    public void setShipHit(int x, int y ){
        opponentBoard[x][y].setStyle("-fx-background-color: "+HITFLOATINGCOLOR);
        printMessage("trafiony!");
    }
    public void setYourShipHit(int x, int y ){
        myBoard[x][y].setShoot();
        myBoard[x][y].setStyle("-fx-background-color: "+HITFLOATINGCOLOR);
        printMessage("Zostales trafiny :(");
    }
    public void setShipSunkHit(int x, int y){
        opponentBoard[x][y].setStyle("-fx-background-color: "+HITSUNKCOLOR);
        printMessage("zatopiony!");
        //ToDO change other ship fields to sunkcolor
    }

    public  void setYourShipSunk(int x, int y){
        myBoard[x][y].setShoot();
        myBoard[x][y].setStyle("-fx-background-color: "+HITSUNKCOLOR);
        printMessage("twoj statek zatonal");
        //ToDO change other ship fields to sunkcolor
    }


    public void printMessage(String text){
        textField.setText(text);
    }
    public void initOpponentBoard(){
        for ( int y = 1; y<=game.BOARDWIDTH;++y){
            for ( int x = 1; x<=game.BOARDWIDTH;++x){
                Cell cell=new Cell (x,y);
                opponentBoard[x][y]=cell;
                opponentGrid.add(cell,y,x);
                cell.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        if(!game.isYourTurn()) {
                            printMessage("ruch przeciwnika, czekaj");
                            return;
                        }
                        printMessage("wybrales pole "+cell.x+ (char)(cell.y+64) );
                        if (cell.wasShoot()==true){
                            printMessage("to polebylo juz przez ciebie wybrane");
                            return;
                        }
                        else {
                            cell.setShoot();
                            game.attackField(cell.x, cell.y);
                        }
                    }
                });
            }
        }
    }
    public void initMyBoard(){
        for ( int y = 1; y<=game.BOARDWIDTH;++y){
            for ( int x = 1; x<=game.BOARDWIDTH;++x){
                Cell cell=new Cell (x,y);
                myGrid.add(cell,y,x);
                myBoard[x][y]=cell;
            }
        }
    }

}
