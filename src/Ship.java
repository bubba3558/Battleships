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
    public Boolean isDestroyed(){
        return condition==0;
    }
    private void takeHit(){
        --condition;
        try {
            if (condition<0)
                throw new Exception("Condition is negative");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
