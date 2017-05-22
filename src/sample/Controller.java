package sample;
import Logic.*;

import exception.CollisionException;
import exception.OutOfBoardException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    Game game;
    Orientation orientation= Orientation.HORIZONTAL;
    @FXML private TextArea textField;
    @FXML private GridPane myGrid;
    @FXML private GridPane opponentGrid;
    @FXML private Shape verticTriangle;
    @FXML private Shape horizTriangle;
    @FXML private Shape triangle5;
    @FXML private Shape triangle4a;
    @FXML private Shape triangle4b;
    @FXML private Shape triangle3a;
    @FXML private Shape triangle3b;
    @FXML private Shape triangle2a;
    @FXML private Shape triangle2b;
    @FXML private Shape triangle2c;
    @FXML private Label orientationText;
    @FXML private Label pointText;

    private Cell [][]opponentBoard;
    private Cell [][]myBoard;
    public final static String HITFLOATINGCOLOR="#ff0303";
    public final static String HITSUNKCOLOR="#930505";
    public final static String MISSCOLOR="#58648c";
    public final static String SHIPCOLOR="#428908";
    public final static int SHIPSNO=7;
    private int placedShipsNo=0;
    private int startX=-1;
    private int startY=-1;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        opponentBoard=new Cell[game.BOARDWIDTH+1][game.BOARDHIGHT+1];
        myBoard=new Cell[game.BOARDWIDTH+1][game.BOARDHIGHT+1];
        initMyBoard();
        initOpponentBoard();
        Platform.setImplicitExit(false);
    }
    public void changeOrientation(ActionEvent event){
        switch (orientation){
            case VERTICAL:
                orientation=Orientation.HORIZONTAL;
                horizTriangle.setOpacity(1);
                verticTriangle.setOpacity(0);
                orientationText.setText("pozioma");
                break;
            case HORIZONTAL:
                orientation=Orientation.VERTICAL;
                horizTriangle.setOpacity(0);
                verticTriangle.setOpacity(1);
                orientationText.setText("pionowa");
        }
    }

    public void setGame(Game game){
        this.game=game;
    }
    public void placeShips(){
        if (placedShipsNo < SHIPSNO){
            printMessage("Umiesc wszystkie statki\n" +
                    "Aby ustawić statek wybierz jego orientację, a następnie kliknij na jego rufę. \n" +
                    "Następnie wybierz na swojej mapie pole gdzie ma znajdować się rufa. ");
            return;
            }
        printMessage("ustawiles statki, czekaj na przeciwnika");
        game.sendReadyMessage();
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
                opponentGrid.add(cell,x,y);
                cell.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        if (!game.isPrepared()){
                            printMessage("czekaj az Ty i przeciwnik ustawicie statki");
                            return;
                        }
                        if(!game.isYourTurn()) {
                            printMessage("ruch przeciwnika, czekaj");
                            return;
                        }
                        printMessage("wybrales pole "+cell.y+ (char)(cell.x+64) );
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
                myGrid.add(cell,x,y);
                myBoard[x][y]=cell;
                cell.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        if (game.isPrepared()){
                            printMessage("ustawiliscie juz statki");
                            return;
                        }
                        startY=cell.y;
                        startX=cell.x;
                        pointText.setText(""+ (char) (startX+64) +startY);
                    }
                });
            }
        }
    }

    public void placeShipL5(){
        if (placeShip(5)){
            triangle5.setDisable(true);
            triangle5.setOpacity(0);
            ++placedShipsNo;
        }
    }
    public void placeShipL4a(){
        if (placeShip(4)){
            triangle4a.setDisable(true);
            triangle4a.setOpacity(0);
            ++placedShipsNo;
        }
    }
    public void placeShipL4b(){
        if (placeShip(4)){
            triangle4b.setDisable(true);
            triangle4b.setOpacity(0);
            ++placedShipsNo;
        }
    }
    public void placeShipL3a(){
        if (placeShip(3)){
            triangle3a.setDisable(true);
            triangle3a.setOpacity(0);
            ++placedShipsNo;
        }
    }
    public void placeShipL3b(){
        if (placeShip(3)){
            triangle3b.setDisable(true);
            triangle3b.setOpacity(0);
            ++placedShipsNo;
        }
    }
    public void placeShipL2a(){
        if (placeShip(2)){
            triangle2a.setDisable(true);
            triangle2a.setOpacity(0);
            ++placedShipsNo;
        }
    }
    public void placeShipL2b(){
        if (placeShip(2)){
            triangle2b.setDisable(true);
            triangle2b.setOpacity(0);
            ++placedShipsNo;
        }
    }
    public void placeShipL2c(){
        if (placeShip(2)){
            triangle2c.setDisable(true);
            triangle2c.setOpacity(0);
            ++placedShipsNo;
        }
    }

    public boolean placeShip(int length){//true-successful
        if(startX==-1) {
            printMessage("wybierz pole dla rufy");
            return false;
        }
        int x=startX, y=startY;                     //in case it will be changed
        Orientation tempOrientation=orientation;
        try {
            game.addShipToBoard(tempOrientation, length, x,y);
        }catch (OutOfBoardException e){
            printMessage("Nie mozna postawic statku w tym miejscu, wychodzi poza pole");
            return false;
        }catch (CollisionException e){
            printMessage("Nie mozna postawic statku w tym miejscu, kolizja z innym statkiem");
            return false;
        }
        if(tempOrientation==Orientation.HORIZONTAL) {
            for (int i = 0; i < length; ++i, ++x)
                myBoard[x][y].setStyle("-fx-background-color: " + SHIPCOLOR);
            return true;
        }
        else {
            for (int i = 0; i < length; ++i, ++y)
                myBoard[x][y].setStyle("-fx-background-color: " + SHIPCOLOR);
            return true;
        }
    }


}
