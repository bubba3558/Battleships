package model;

import exception.CollisionException;
import exception.OutOfBoardException;
import gui.Controller;
import javafx.application.Platform;
import network.Message;
import network.NetworkManager;

public class Game {
    public static final int BOARDHIGHT = 15;
    public static final int BOARDWIDTH = 15;
    private Board board;
    public NetworkManager netManager;
    private boolean isYourTurn;
    private int score = 0;
    private boolean youAreReady = false;
    private boolean opponentIsReady = false;
    private boolean gamePrepared = false;
    private boolean gameEnd = false;
    private Controller controller;

    public Game(Boolean isHost, NetworkManager networkManager, Controller controller) {
        this.netManager = networkManager;
        board = new Board(BOARDHIGHT, BOARDWIDTH);
        netManager.setGame(this);
        isYourTurn = isHost;
        this.controller = controller;
    }

    public boolean isYourTurn() {
        return isYourTurn;
    }

    public void takeHit(int x, int y) throws Exception {
        if (board.takeHit(x, y)) {
            --score;
            if (board.isShipFloating(x, y)) {
                netManager.sendMessage(Message.getHitNotSunkMessage(x, y));
                controller.setYourShipHit(x, y);
            } else {
                Point point = board.getBow(x, y);
                x = point.getX();
                y = point.getY();
                netManager.sendMessage(Message.getHitAndSunkMessage(x, y, board.getShipLength(x, y), board.getOrientation(x, y)));
                controller.setYourShipSunk(x, y, board.getShipLength(x, y), board.getOrientation(x, y));
            }
            if (score == 0) {
                netManager.sendMessage(Message.getYouWonMessage(x, y));
                controller.printMessage("przegrałes");
                gameEnd = true;
            }
        } else {
            netManager.sendMessage(Message.getMissMessage(x, y));
            isYourTurn = true;
            controller.setOpponentMiss(x, y);
        }
    }

    public void handleMessage(Message message) throws Exception {

        switch (message.getType()) {
            case ATTACK:
                takeHit(message.getX(), message.getY());
                break;
            case MISS:
                isYourTurn = false;
                controller.setMiss(message.getX(), message.getY());
                break;
            case SHIPHIT:
                if (message.getFloating() == true)
                    controller.setShipHit(message.getX(), message.getY());
                else
                    controller.setShipSunkHit(message.getX(), message.getY(), message.getShipLength(), message.getOrientation());
                break;
            case READYTOPLAY:
                opponentIsReady = true;
                controller.printMessage("przeciwnik ustawil statki");
                checkIfGameIsReady();
                break;
            case GAME_END:
                controller.printMessage("wygrałeś");
                gameEnd = true;
                break;
            case RESTART:
                restart();
        }
        return;
    }

    public void attackField(int x, int y) {
        netManager.sendMessage(Message.getAttackMessage(x, y));
    }

    private void checkIfGameIsReady() {
        if (youAreReady && opponentIsReady) {
            gamePrepared = true;
            if (isYourTurn())
                controller.printMessage("Twoja kolej");
            else
                controller.printMessage("czekaj na ruch przeciwnika");
        }
    }

    public void sendReadyMessage() {
        youAreReady = true;
        checkIfGameIsReady();
        netManager.sendMessage(Message.getReadyToPlayMessage());
    }

    public boolean isPrepared() {
        return gamePrepared;
    }

    public void addShipToBoard(Orientation orientation, int length, int startX, int startY) throws OutOfBoardException, CollisionException {
        Ship ship = new Ship(length, orientation);
        if (board.placeShip(startX, startY, ship)) {
            score += ship.getSize();
            ship.setBow(new Point(startX, startY));
        }
    }

    public boolean isTaken(int x, int y) {
        if (!board.isFieldInsideBoard(x, y))
            return true;
        return (board.getFieldType(x, y) == FieldType.WITHSHIP || board.getFieldType(x, y) == FieldType.NEARSHIP);
    }

    public void haveLostConnection() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.printErrorMessage("Utraciłeś połączenie, uruchom gre jeszcze raz");
            }
        });
        gamePrepared = false;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public void setRestart() {
        netManager.sendMessage(Message.getRestartMessage());
        restart();
    }

    public void restart() {
        board = new Board(BOARDHIGHT, BOARDWIDTH);
        score = 0;
        youAreReady = false;
        opponentIsReady = false;
        gamePrepared = false;
        gameEnd = false;
        controller.restartController();
    }
}
