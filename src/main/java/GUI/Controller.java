package GUI;

import Logic.*;

import exception.CollisionException;
import exception.OutOfBoardException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    Game game;
    Orientation orientation = Orientation.HORIZONTAL;
    @FXML
    private TextArea textField;
    @FXML
    private GridPane myGrid;
    @FXML
    private GridPane opponentGrid;
    @FXML
    private Shape verticTriangle;
    @FXML
    private Shape horizTriangle;
    @FXML
    private Shape triangle5;
    @FXML
    private Shape triangle4a;
    @FXML
    private Shape triangle4b;
    @FXML
    private Shape triangle3a;
    @FXML
    private Shape triangle3b;
    @FXML
    private Shape triangle2a;
    @FXML
    private Shape triangle2b;
    @FXML
    private Shape triangle2c;
    @FXML
    private Label orientationText;
    @FXML
    private Label errorField;

    private Cell[][] opponentBoard;
    private Cell[][] myBoard;
    public final static String HITFLOATINGCOLOR = "#ff0303";
    public final static String HITSUNKCOLOR = "#930505";
    public final static String MISSCOLOR = "#58648c";
    public final static String SHIPCOLOR = "#428908";
    public final static String WATERCOLOR = "#5b7cea";
    public final static String SAFETYCOLOR = "#8898cc";

    public final static int SHIPSNO = 8;
    private int shipsToPlace = SHIPSNO;
    private int startX = -1;
    private int startY = -1;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initMyBoard();
        initOpponentBoard();
        Platform.setImplicitExit(false);
    }

    public void changeOrientation(ActionEvent event) {
        switch (orientation) {
            case VERTICAL:
                orientation = Orientation.HORIZONTAL;
                horizTriangle.setOpacity(1);
                verticTriangle.setOpacity(0);
                orientationText.setText("pozioma");
                break;
            case HORIZONTAL:
                orientation = Orientation.VERTICAL;
                horizTriangle.setOpacity(0);
                verticTriangle.setOpacity(1);
                orientationText.setText("pionowa");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setMiss(int x, int y) {
        opponentBoard[x][y].setStyle("-fx-background-color: " + MISSCOLOR);//getChildren().add(new Rectangle(15,15));
        printMessage("Pudło, kolej przeciwnika");
    }

    public void setOpponentMiss(int x, int y) {
        myBoard[x][y].setShoot();
        myBoard[x][y].setStyle("-fx-background-color: " + MISSCOLOR);
        printMessage("Przeciwnik spudłowal. Twoja kolej");
    }

    public void setShipHit(int x, int y) {
        opponentBoard[x][y].setStyle("-fx-background-color: " + HITFLOATINGCOLOR);
        printMessage("Trafiony!");
    }

    public void setYourShipHit(int x, int y) {
        myBoard[x][y].setStyle("-fx-background-color: " + HITFLOATINGCOLOR);
        printMessage("Zostałeś trafiny :(");
    }

    public void setShipSunkHit(int x, int y, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            setSafetyColumn(x - 1, y);
            for (int i = 0; i < length; ++i, ++x) {
                setSafetyColumn(x, y);
                opponentBoard[x][y].setStyle("-fx-background-color: " + HITSUNKCOLOR);
            }
            setSafetyColumn(x, y);
        } else {
            setSafetyRow(x, y - 1);
            for (int i = 0; i < length; ++i, ++y) {
                setSafetyRow(x, y);
                opponentBoard[x][y].setStyle("-fx-background-color: " + HITSUNKCOLOR);
            }
            setSafetyRow(x, y);
        }
        printMessage("Zatopiony!");
    }

    private void setSafetyColumn(int x, int y) {
        setSafetyField(x, y);
        setSafetyField(x, y - 1);
        setSafetyField(x, y + 1);
    }

    private void setSafetyRow(int x, int y) {
        setSafetyField(x - 1, y);
        setSafetyField(x, y);
        setSafetyField(x + 1, y);
    }

    private void setSafetyField(int x, int y) {
        opponentBoard[x][y].setShoot();
        opponentBoard[x][y].setStyle("-fx-background-color: " + SAFETYCOLOR);
    }

    public void setYourShipSunk(int x, int y, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL)
            for (int i = 0; i < length; ++i, ++x) {
                myBoard[x][y].setStyle("-fx-background-color: " + HITSUNKCOLOR);
            }
        else
            for (int i = 0; i < length; ++i, ++y) {
                myBoard[x][y].setStyle("-fx-background-color: " + HITSUNKCOLOR);
            }
        printMessage("Twój statek zatonął.");
        //ToDO change other ship fields to sunkcolor
    }


    public void printMessage(String text) {
        textField.setText(text);
    }
    public void printErrorMessage(String text) {
        errorField.setText(text);
    }


    public void initOpponentBoard() {

        opponentBoard = new Cell[game.BOARDWIDTH + 2][game.BOARDHIGHT + 2];
        for (int y = 0; y <= game.BOARDWIDTH + 1; ++y) {
            for (int x = 0; x <= game.BOARDWIDTH + 1; ++x) {
                Cell cell = new Cell(x, y);
                opponentBoard[x][y] = cell;
                if (x == 0 || y == 0 || y == game.BOARDWIDTH + 1 || x == game.BOARDHIGHT + 1)
                    continue; //add safety frame
                opponentGrid.add(cell, x, y);
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        if (game.isGameEnd()) {
                            printMessage("Gra zakończona, kliknij restart aby zacząć kolejną.");
                            return;

                        }
                        if (!game.isPrepared()) {
                            printMessage("Czekaj aż Ty i przeciwnik ustawicie statki.");
                            return;
                        }
                        if (!game.isYourTurn()) {
                            printMessage("Ruch przeciwnika, czekaj.");
                            return;
                        }
                        printMessage("Wybraeś pole " + cell.y + (char) (cell.x + 64));
                        if (cell.wasShoot() == true) {
                            printMessage("To pole było już przez Ciebie wybrane");
                            return;
                        } else {
                            cell.setShoot();
                            game.attackField(cell.x, cell.y);
                        }
                    }
                });
            }
        }
    }

    public void initMyBoard() {
        myBoard = new Cell[game.BOARDWIDTH + 2][game.BOARDHIGHT + 2];
        for (int y = 0; y <= game.BOARDWIDTH + 1; ++y) {
            for (int x = 0; x <= game.BOARDWIDTH + 1; ++x) {
                Cell cell = new Cell(x, y);
                myBoard[x][y] = cell;
                if (x == 0 || y == 0 || y == game.BOARDWIDTH + 1 || x == game.BOARDHIGHT + 1)
                    continue; //add safety frame
                myGrid.add(cell, x, y);
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        if (shipsToPlace == 0) {
                            printMessage("ustawiles juz statki");
                            return;
                        }
                        if (!game.isTaken(startX, startY)) {
                            myBoard[startX][startY].setStyle("-fx-background-color: " + WATERCOLOR);
                        }
                        startY = cell.y;
                        startX = cell.x;
                        if (!game.isTaken(startX, startY))
                            cell.setStyle("-fx-background-color: " + MISSCOLOR);
                        printMessage("Rufa znajdzie się na polu: " + (char) (startX + 64) + startY);
                    }
                });
            }
        }
    }

    public void placeShipL5() {
        if (placeShip(5)) {
            triangle5.setDisable(true);
            triangle5.setOpacity(0);
        }
    }

    public void placeShipL4a() {
        if (placeShip(4)) {
            triangle4a.setDisable(true);
            triangle4a.setOpacity(0);
        }
    }

    public void placeShipL4b() {
        if (placeShip(4)) {
            triangle4b.setDisable(true);
            triangle4b.setOpacity(0);
        }
    }

    public void placeShipL3a() {
        if (placeShip(3)) {
            triangle3a.setDisable(true);
            triangle3a.setOpacity(0);
        }
    }

    public void placeShipL3b() {
        if (placeShip(3)) {
            triangle3b.setDisable(true);
            triangle3b.setOpacity(0);
        }
    }

    public void placeShipL2a() {
        if (placeShip(2)) {
            triangle2a.setDisable(true);
            triangle2a.setOpacity(0);
        }
    }

    public void placeShipL2b() {
        if (placeShip(2)) {
            triangle2b.setDisable(true);
            triangle2b.setOpacity(0);
        }
    }

    public void placeShipL2c() {
        if (placeShip(2)) {
            triangle2c.setDisable(true);
            triangle2c.setOpacity(0);
        }
    }

    public boolean placeShip(int length) {//true-successful
        if (startX == -1) {
            printMessage("Wybierz pole dla rufy");
            return false;
        }
        if (shipsToPlace == 0) {
            printMessage("Ustawiłeś już statki");
            return false;
        }
        int x = startX, y = startY;                     //in case it will be changed
        Orientation tempOrientation = orientation;
        try {
            game.addShipToBoard(tempOrientation, length, x, y);
        } catch (OutOfBoardException e) {
            printMessage("Nie można postawić statku w tym miejscu, wychodzi poza pole");
            return false;
        } catch (CollisionException e) {
            printMessage("Nie można postawić statku w tym miejscu, kolizja z innym statkiem");
            return false;
        }
        colorShipFieldsAndSafetyZone(tempOrientation, length, x, y);
        --shipsToPlace;
        if (shipsToPlace == 0) {
            printMessage("Ustawiłeś statki. Czekaj na przeciwnika");
            game.sendReadyMessage();
        }

        return true;
    }

    private void colorShipFieldsAndSafetyZone(Orientation orientation, int length, int x, int y) {
        if (orientation == Orientation.HORIZONTAL) {
            setMySafetyColumn(x - 1,y);
            for (int i = 0; i < length; ++i, ++x) {
                setMySafetyColumn(x,y);
                myBoard[x][y].setStyle("-fx-background-color: " + SHIPCOLOR);
            }
            setMySafetyColumn(x,y);
        } else {
            setMySafetyRow(x,y-1);
            for (int i = 0; i < length; ++i, ++y) {
                setMySafetyRow(x,y);
                myBoard[x][y].setStyle("-fx-background-color: " + SHIPCOLOR);
            }
            setMySafetyRow(x,y);
        }
    }
    private void setMySafetyColumn(int x, int y) {
        setMySafetyField(x, y);
        setMySafetyField(x, y - 1);
        setMySafetyField(x, y + 1);
    }

    private void setMySafetyRow(int x, int y) {
        setMySafetyField(x - 1, y);
        setMySafetyField(x, y);
        setMySafetyField(x + 1, y);
    }

    private void setMySafetyField(int x, int y) {
        myBoard[x][y].setShoot();
        myBoard[x][y].setStyle("-fx-background-color: " + SAFETYCOLOR);
    }

    public void restartGame() {
        if (!game.isGameEnd()) {
            printMessage("Zakończ gre, aby ją zrestartować");
            return;
        }
        game.setRestart();
    }

    public void restartController() {
        shipsToPlace = SHIPSNO;
        resetBoard(myBoard);
        resetBoard(opponentBoard);
        setTrianglesVisible();
    }

    public void resetBoard(Cell[][] board) {
        for (int y = 0; y <= game.BOARDWIDTH + 1; ++y) {
            for (int x = 0; x <= game.BOARDWIDTH + 1; ++x) {
                board[x][y].reset();
            }
        }
    }

    public void setTrianglesVisible() {

        triangle5.setDisable(false);
        triangle5.setOpacity(1);
        triangle4a.setDisable(false);
        triangle4a.setOpacity(1);
        triangle4b.setDisable(false);
        triangle4b.setOpacity(1);
        triangle3a.setDisable(false);
        triangle3a.setOpacity(1);
        triangle3b.setDisable(false);
        triangle3b.setOpacity(1);
        triangle2a.setDisable(false);
        triangle2a.setOpacity(1);
        triangle2b.setDisable(false);
        triangle2b.setOpacity(1);
        triangle2c.setDisable(false);
        triangle2c.setOpacity(1);
    }
}
// komunikat