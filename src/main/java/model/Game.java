package model;

import exception.CollisionException;
import exception.OutOfBoardException;
import gui.GameControllerInterface;
import network.Message;
import network.NetworkManager;

import static model.GameMessageType.opponentPlacedHisShips;
import static model.GameMessageType.waitForOpponentMove;
import static model.GameMessageType.yourTurn;

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
    private GameControllerInterface gameInterface;

    public Game(Boolean isHost, NetworkManager networkManager, GameControllerInterface gameInterface) {
        this.netManager = networkManager;
        board = new Board(BOARDHIGHT, BOARDWIDTH);
        netManager.setGame(this);
        isYourTurn = isHost;
        this.gameInterface = gameInterface;
    }

    public boolean isYourTurn() {
        return isYourTurn;
    }

    public void takeHit(int x, int y) throws Exception {
        if (board.takeHit(x, y)) {
            --score;
            if (board.isShipFloating(x, y)) {
                netManager.sendMessage(Message.getHitNotSunkMessage(x, y));
                gameInterface.setYourShipHit(x, y);
            } else {
                Point point = board.getBow(x, y);
                x = point.getX();
                y = point.getY();
                netManager.sendMessage(Message.getHitAndSunkMessage(x, y, board.getShipLength(x, y), board.getOrientation(x, y)));
                gameInterface.setYourShipSunk(x, y, board.getShipLength(x, y), board.getOrientation(x, y));
            }
            if (score == 0) {
                netManager.sendMessage(Message.getYouWonMessage(x, y));
                gameInterface.setLost();
                gameEnd = true;
            }
        } else {
            netManager.sendMessage(Message.getMissMessage(x, y));
            isYourTurn = true;
            gameInterface.setOpponentMiss(x, y);
        }
    }

    public void handleMessage(Message message) throws Exception {

        switch (message.getType()) {
            case ATTACK:
                takeHit(message.getX(), message.getY());
                break;
            case MISS:
                isYourTurn = false;
                gameInterface.setMiss(message.getX(), message.getY());
                break;
            case SHIPHIT:
                if (message.getFloating() == true) {
                    gameInterface.setShipHit(message.getX(), message.getY());
                } else {
                    gameInterface.setShipSunkHit(message.getX(), message.getY(), message.getShipLength(), message.getOrientation());
                }
                break;
            case READYTOPLAY:
                opponentIsReady = true;
                gameInterface.setMessage(opponentPlacedHisShips);
                checkIfGameIsReady();
                break;
            case GAME_END:
                gameInterface.setWin();
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
            if (isYourTurn()) {
                gameInterface.setMessage(yourTurn);
            } else {
                gameInterface.setMessage(waitForOpponentMove);
            }
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
        if (!board.isFieldInsideBoard(x, y)) {
            return true;
        }
        return (board.getFieldType(x, y) == FieldType.WITHSHIP || board.getFieldType(x, y) == FieldType.NEARSHIP);
    }

    public void haveLostConnection() {
        gamePrepared = false;
        gameInterface.setLostConnection();
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
        gameInterface.restartView();
    }
}
