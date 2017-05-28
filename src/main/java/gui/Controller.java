package gui;

import exception.CollisionException;
import exception.OutOfBoardException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Shape;
import model.Game;
import model.GameMessageType;
import model.Orientation;
import model.Point;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable, GameControllerInterface {
    Game game;
    Orientation orientation = Orientation.HORIZONTAL;
    @FXML
    private TextArea textField;
    @FXML
    private GridPane myGrid;
    @FXML
    private GridPane opponentGrid;
    @FXML
    private GridPane shipsBoard;
    @FXML
    private GridPane orientationPane;
    @FXML
    private Shape verticalTriangle;
    @FXML
    private Shape horizontalTriangle;
    @FXML
    private Label orientationText;
    @FXML
    private Label errorField;
    @FXML
    private Button restartButton;
    @FXML
    private Button orientationButton;

    private Cell[][] opponentBoard;
    private Cell[][] myBoard;
    private ArrayList<ShipPane> shipPaneList = new ArrayList<>();
    public final static String HITFLOATINGCOLOR = "#ff0303";
    public final static String HITSUNKCOLOR = "#930505";
    public final static String MISSCOLOR = "#58648c";
    public final static String SHIPCOLOR = "#428908";
    public final static String SAFETYCOLOR = "#8898cc";

    public final static int[] shipsAmountOfType = new int[]{0, 3, 2, 2, 1};  //here 0 ships with length 1, 3 ships length 2, 2 ships length 3 and so on.
    public int shipsCount;
    private int shipsToPlace = 0;
    private int startX = -1;
    private int startY = -1;
    private Point lastShipIndex;

    /**
     * shipsAmountOfType contains how many ships of length 1,2,3,4.. should be placed
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int count : shipsAmountOfType) {
            shipsToPlace += count;
        }
        shipsCount = shipsToPlace;
        initMyBoard();
        initOpponentBoard();
        Platform.setImplicitExit(false);
        initShipsBoard();
    }

    public void changeOrientation(ActionEvent event) {
        switch (orientation) {
            case VERTICAL:
                orientation = Orientation.HORIZONTAL;
                horizontalTriangle.setOpacity(1);
                verticalTriangle.setOpacity(0);
                orientationText.setText("pozioma");
                break;
            case HORIZONTAL:
                orientation = Orientation.VERTICAL;
                horizontalTriangle.setOpacity(0);
                verticalTriangle.setOpacity(1);
                orientationText.setText("pionowa");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setMiss(int x, int y) {
        opponentBoard[x][y].setColor(MISSCOLOR);
        printMessage("Pudło, kolej przeciwnika");
    }

    public void setOpponentMiss(int x, int y) {
        myBoard[x][y].setShoot();
        myBoard[x][y].setColor(MISSCOLOR);
        printMessage("Przeciwnik spudłowal. Twoja kolej");
    }

    public void setShipHit(int x, int y) {
        opponentBoard[x][y].setColor(HITFLOATINGCOLOR);
        printMessage("Trafiony!");
    }

    public void setYourShipHit(int x, int y) {
        myBoard[x][y].setColor(HITFLOATINGCOLOR);
        printMessage("Zostałeś trafiny :(");
    }

    public void setShipSunkHit(int x, int y, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            setSafetyColumn(x - 1, y);
            for (int i = 0; i < length; ++i, ++x) {
                setSafetyColumn(x, y);
                opponentBoard[x][y].setColor(HITSUNKCOLOR);
            }
            setSafetyColumn(x, y);
        } else {
            setSafetyRow(x, y - 1);
            for (int i = 0; i < length; ++i, ++y) {
                setSafetyRow(x, y);
                opponentBoard[x][y].setColor(HITSUNKCOLOR);
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
        opponentBoard[x][y].setColor(SAFETYCOLOR);
    }

    public void setYourShipSunk(int x, int y, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL)
            for (int i = 0; i < length; ++i, ++x) {
                myBoard[x][y].setColor(HITSUNKCOLOR);
            }
        else
            for (int i = 0; i < length; ++i, ++y) {
                myBoard[x][y].setColor(HITSUNKCOLOR);
            }
        printMessage("Twój statek zatonął.");
    }
    public void setMessage(GameMessageType messageType){
        String text = "błąd";
        switch (messageType){
            case opponentPlacedHisShips:
                text = "przeciwnik ustawil swoje statki";
                break;
            case yourTurn:
                text = "Twoja kolej, wybierz pole przeciwnika, które chcesz zaatakować";
                break;
            case waitForOpponentMove:
                text = "Poczekaj na ruch przeciwnika";
                break;
        }
        printMessage(text);
    }

    private void printMessage(String text) {
        textField.setText(text);
    }

    public void setErrorMessage(String text) {
        errorField.setText(text);
    }

    public void initOpponentBoard() {

        opponentBoard = new Cell[Game.BOARDWIDTH + 2][Game.BOARDHIGHT + 2];
        for (int y = 0; y <= Game.BOARDWIDTH + 1; ++y) {
            for (int x = 0; x <= Game.BOARDWIDTH + 1; ++x) {
                Cell cell = new Cell(x, y);
                opponentBoard[x][y] = cell;
                /**add safety frame*/
                if (x == 0 || y == 0 || y == Game.BOARDWIDTH + 1 || x == Game.BOARDHIGHT + 1) {
                    continue;
                }
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
        myBoard = new Cell[Game.BOARDWIDTH + 2][Game.BOARDHIGHT + 2];
        for (int y = 0; y <= Game.BOARDWIDTH + 1; ++y) {
            for (int x = 0; x <= Game.BOARDWIDTH + 1; ++x) {
                Cell cell = new Cell(x, y);
                myBoard[x][y] = cell;
                if (x == 0 || y == 0 || y == Game.BOARDWIDTH + 1 || x == Game.BOARDHIGHT + 1) {
                    continue;
                }
                myGrid.add(cell, x, y);
                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    /**change previous clicked field's color to WATERCOLOR if ship was not placed*/
                    public void handle(MouseEvent t) {
                        if (shipsToPlace == 0) {
                            printMessage("ustawiles juz statki");
                            return;
                        }
                        if (!game.isTaken(startX, startY)) {
                            myBoard[startX][startY].resetColor();
                        }
                        startY = cell.y;
                        startX = cell.x;
                        if (!game.isTaken(startX, startY))
                            cell.setColor(MISSCOLOR);
                        printMessage("Rufa znajdzie się na polu: " + (char) (startX + 64) + startY);
                    }
                });
            }
        }
    }

    private void colorShipFieldsAndSafetyZone(Orientation orientation, int length, int x, int y) {
        if (orientation == Orientation.HORIZONTAL) {
            setMySafetyColumn(x - 1, y);
            for (int i = 0; i < length; ++i, ++x) {
                setMySafetyColumn(x, y);
                myBoard[x][y].setColor(SHIPCOLOR);
            }
            setMySafetyColumn(x, y);
        } else {
            setMySafetyRow(x, y - 1);
            for (int i = 0; i < length; ++i, ++y) {
                setMySafetyRow(x, y);
                myBoard[x][y].setColor(SHIPCOLOR);
            }
            setMySafetyRow(x, y);
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
        myBoard[x][y].setColor(SAFETYCOLOR);
    }

    public void restartGame() {
        game.setRestart();
    }

    public void restartView() {
        restartButton.setOpacity(0);
        restartButton.setDisable(true);
        orientationButton.setDisable(false);
        orientationPane.setOpacity(1);
        shipsToPlace = shipsCount;
        for (ShipPane pane : shipPaneList) {
            pane.reset();
        }
        resetBoard(myBoard);
        resetBoard(opponentBoard);
    }

    public void resetBoard(Cell[][] board) {
        for (int y = 0; y <= Game.BOARDWIDTH + 1; ++y) {
            for (int x = 0; x <= Game.BOARDWIDTH + 1; ++x) {
                board[x][y].reset();
            }
        }
    }

    public void setWin() {
        printMessage("wygrałeś");
        gameEnd();

    }

    public void setLost() {
        printMessage("przegrałes");
        gameEnd();
    }

    private void gameEnd() {
        restartButton.setOpacity(1);
        restartButton.setDisable(false);
    }

    public void initShipsBoard() {

        lastShipIndex = new Point(0, 0);
        for (int i = 0; i < shipsAmountOfType.length; ++i) {
            for (int j = 0; j < shipsAmountOfType[i]; ++j) {
                ShipPane pane = new ShipPane(i + 1);/**+1 couse tab index starts from 0 and length of Ship = 0 is pointless so is not include in shipsAmountOfType*/
                shipPaneSetOnAction(pane);
                shipsBoard.add(pane, lastShipIndex.x, lastShipIndex.y);
                incrementLastShipIndex();
                shipPaneList.add(pane);
            }
        }
    }

    /**
     * grid has two rows so x can be 0 or 1
     */

    public void incrementLastShipIndex() {
        if (lastShipIndex.x == 0) {
            ++lastShipIndex.x;
            return;
        } else {
            lastShipIndex.x = 0;
            ++lastShipIndex.y;
        }
    }

    /**
     * return true if successful
     */
    public boolean placeShip(int length) {
        if (startX == -1) {
            printMessage("Wybierz pole dla rufy");
            return false;
        }
        if (shipsToPlace == 0) {
            printMessage("Ustawiłeś już statki");
            return false;
        }
        int x = startX, y = startY;
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
            orientationButton.setDisable(true);
            orientationPane.setOpacity(0);
        }

        return true;
    }

    private void shipPaneSetOnAction(ShipPane pane) {
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override

            public void handle(MouseEvent t) {
                if (placeShip(pane.getLength())) {
                    pane.setPlaced();
                }
            }
        });
    }

    public void setLostConnection(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setErrorMessage("Utraciłeś połączenie, uruchom gre jeszcze raz");
            }
        });
    }
}