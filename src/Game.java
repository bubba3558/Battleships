import javafx.geometry.HorizontalDirection;

/**
 * Created by Martyna on 12.05.2017.
 */
public class Game {
    private static Board board;
    private int shipNo = 5;
    private int score = 0;
    private NetworkManager netManager;

    public static void main(String[] args){
        board=new Board(20,20);

        playRound();
    }
    public Game (){
        board=new Board(20,20);
        NetworkManager=new NetworkManager(this,)
    }
    public static boolean playRound(){
       //debug
  return true;
    }
    public void handleMessage(Message message){
        System.out.println("heh");
        return;
    }
}
