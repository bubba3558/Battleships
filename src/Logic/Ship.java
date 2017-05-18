package Logic;

import com.sun.javafx.css.Size;

/**
 * Created by Martyna on 10.05.2017.
 */
public class Ship {
    private int size;
    private int condition;
    private Orientation orientation;


    public Ship(int size, Orientation orientation){
        this.size=size;
        this.orientation=orientation;
        this.condition=size;
    }

    public Orientation getOrientation(){
        return orientation;
    }
    public int getCondition(){
        return condition;
    }
    public int getSize(){
        return size;
    }
    public Boolean isDestroyed(){
        return condition==0;
    }
    public void takeHit(){
        --condition;
//        try { //dont need
//            if (condition<0)
//                throw new Exception("Condition is negative");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
