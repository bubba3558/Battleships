import javafx.geometry.HorizontalDirection;

/**
 * Created by Martyna on 12.05.2017.
 */
public class Game {
    private static final int BOARDHIGHT = 20;
    private static final int BOARDWIDTH = 20;
    private Board board;
    private int shipNo = 5;
    private int score = 0;
    private NetworkManager netManager;

    public Game (Boolean isHost, int portNo, String serverIP){
        board=new Board(BOARDHIGHT,BOARDWIDTH);
        netManager=new NetworkManager(this, isHost, portNo, serverIP);
    }
    public void main(String[] args){
        netManager.run();
        placeShips();

        playRound();
    }
    public static boolean playRound(){
       //debug
  return true;
    }
    public void handleMessage(Message message)throws Exception{
        switch (message.getType()){
            case ATTACK:
                if (board.takeHit(message.getX(), message.getY())){
                    if(board.isShipFloating(message.getX(), message.getY()))
                        netManager.sendMessage(Message.getHitNotSunkMessage());
                    else
                        netManager.sendMessage(Message.getHitAndSunkMessage());
                }
                else {
                    netManager.sendMessage(Message.AttackMessage());
                }

        }
            case
        return;
    }
}
