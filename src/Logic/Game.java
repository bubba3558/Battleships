package Logic;

import Network.*;
import exception.CollisionException;
import exception.OutOfBoardException;
import sample.Controller;

import java.awt.Point;

/**
 * Created by Martyna on 12.05.2017.
 */
public class Game {
    private GameStage stage=GameStage.START;
    public static final int BOARDHIGHT = 15;
    public static final int BOARDWIDTH = 15;
    private Board board;
    private int shipNo = 0;
    private int score=0;
    private NetworkManager netManager;
    private boolean isYourTurn;
    private boolean youAreReady=false;
    private boolean opponentIsReady=false;
    private boolean gamePrepared=false;
    private Controller controller;

    public Game (Boolean isHost, int portNo, String serverIP,Controller controller){
        board=new Board(BOARDHIGHT,BOARDWIDTH);
        netManager=new NetworkManager(this, isHost, portNo, serverIP);
        netManager.run();
        this.controller=controller;
        isYourTurn=isHost;
    }

    public GameStage getStage(){
        return stage;
    }

    public void setStage(GameStage stage){
        this.stage=stage;
    }

    public boolean isYourTurn() {
        return  isYourTurn;
    }
    public void takeHit(int x, int y)throws Exception{
        if (board.takeHit(x, y) ){
            --score;
            System.out.println(score);
            if(board.isShipFloating(x, y)) {
                netManager.sendMessage(Message.getHitNotSunkMessage(x, y));
                controller.setYourShipHit(x, y);
            }
            else {
                Point point=board.getBow(x,y);
                x=point.x;
                y=point.y;
                netManager.sendMessage( Message.getHitAndSunkMessage( x, y, board.getShipLength(x,y), board.getOrientation(x,y)) );
                controller.setYourShipSunk(x, y, board.getShipLength( x, y), board.getOrientation(x,y));
            }
            if(score==0) {
                netManager.sendMessage(Message.getYouWonMessage(x, y));
                controller.printMessage("przegrałes");
            }
        }
        else {
            netManager.sendMessage(Message.getMissMessage(x,y) );
            isYourTurn=true;
            controller.setOpponentMiss(x,y);
        }
    }
    public void main(String[] args){
        netManager.run();
        //placeShips();

        playRound();
    }
    public boolean playRound(){
       //debug
  return true;
    }
    public void handleMessage(Message message)throws Exception{

        switch (message.getType()){
            case ATTACK:
                takeHit(message.getX(), message.getY() );
                break;
            case MISS:
                isYourTurn=false;
                controller.setMiss(message.getX(), message.getY());
                break;
            case SHIPHIT:
                if(message.getFloating()==true)
                    controller.setShipHit( message.getX(), message.getY() );
                else
                    controller.setShipSunkHit( message.getX(), message.getY(), message.getShipLength(), message.getOrientation());
                break;
            case READYTOPLAY:
                opponentIsReady=true;
                controller.printMessage("przeciwnik ustawil statki");
                checkIfGameIsReady();
                break;
            case GAME_END:
                controller.printMessage("wygrałeś");
                gamePrepared=false;
                break;
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
    public void sendReadyMessage(){
        netManager.sendMessage(Message.getReadyToPlayMessage());
        youAreReady=true;
        checkIfGameIsReady();
    }
    public boolean isPrepared(){
        return gamePrepared;
    }
    public void addShipToBoard(Orientation orientation,int length, int startX, int startY )throws OutOfBoardException, CollisionException {
        Ship ship =new Ship(length, orientation);
        if(board.placeShip(startX, startY, ship) ){
            ++shipNo;
            score+=ship.getSize();
            ship.setBow(new Point(startX, startY));
        }
    }
    public boolean isTaken(int x, int y){
        if (! board.isFieldInsideBoard(x, y))
            return true;
        return ( board.getFieldType(x,y)==FieldType.WITHSHIP || board.getFieldType(x,y)==FieldType.NEARSHIP );
    }

}
