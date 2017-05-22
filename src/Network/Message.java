package Network;

import Logic.Orientation;

import java.io.Serializable;

/**
 * Created by Martyna on 14.05.2017.
 */
public class Message  implements Serializable {
     private MessageType type;
     private int x;
     private int y;
     private int shipSize;
     private boolean stillFloating; //if ship is not destroyed after a hit
     private Orientation orientation;

    private Message(MessageType type){//for ready
        this.type=type;
    }

    private Message(MessageType type, int x, int y){ //for attack message
        this.type=type;
        this.x=x;
        this.y=y;
    }
    private Message(MessageType type,int x, int y, boolean sunk){ //for hitShip message
        this.type=type;
        this.stillFloating=!sunk;
        this.x=x;
        this.y=y;
    }
    private Message(MessageType type, int x, int y, boolean sunk, int shipSize, Orientation orientation){ //for hitShip message
        this.type=type;
        this.stillFloating=!sunk;
        this.x=x;
        this.y=y;
        this.shipSize=shipSize;
        this.orientation=orientation;
    }

    public MessageType getType(){
        return type;
    }
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public int getShipLength(){
        return shipSize;
    }
    public Orientation getOrientation(){
        return orientation;
    }
    public boolean getFloating(){return stillFloating;}
    public static Message getReadyToPlayMessage (){
        return new Message (MessageType.READYTOPLAY);
    }

    public static Message getAttackMessage ( int x, int y){
        return new Message (MessageType.ATTACK, x, y);
    }

    public static Message getMissMessage ( int x, int y ){return new Message (MessageType.MISS,x, y);}
    public static Message getHitNotSunkMessage ( int x, int y ){
        return new Message (MessageType.SHIPHIT, x, y,false);
    }

    public static Message getHitAndSunkMessage (int x, int y,int shipSize, Orientation orientation){
        return new Message (MessageType.SHIPHIT, x, y, true, shipSize, orientation);
    }

    public static Message getYouWonMessage (int x, int y){
        return new Message (MessageType.GAME_END,x,y);
    }
    public static Message getErrorMessage (){
        return new Message (MessageType.ERROR);
    }
}
