/**
 * Created by Martyna on 14.05.2017.
 */
public class Message {
     private MessageType type;
     private int x;
     private int y;
     private boolean stillFloating; //if ship is not destroyed after a hit

    private Message(MessageType type){//for ready
        this.type=type;
    }

    private Message(MessageType type, int x, int y){ //for attack message
        this.type=type;
        this.x=x;
        this.y=y;
    }
    private Message(MessageType type, boolean sunk){ //for hitShip message
        this.type=type;
        this.stillFloating=!sunk;
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
    public static Message getReadyToPlayMessage (){
        return new Message (MessageType.READYTOPLAY);
    }

    public static Message getMissAndAttackMessage ( int x, int y){
        return new Message (MessageType.ATTACK, x, y);
    }

    public static Message getHitNotSunkMessage (){
        return new Message (MessageType.SHIPHIT, false);
    }

    public static Message getHitAndSunkMessage (){
        return new Message (MessageType.SHIPHIT, true);
    }

    public static Message getEndMessage (){
        return new Message (MessageType.GAME_END, true);
    }
    public static Message getErrorMessage (){
        return new Message (MessageType.ERROR, true);
    }
}
