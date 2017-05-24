package Logic;

import Logic.Ship;

import java.awt.Point;

/**
 * Created by Martyna on 10.05.2017.
 */
public class Field {

    private FieldType type;
    private Ship ship;

    public Field() {
        type= FieldType.EMPTY;
        ship=null;
    }
//    public Logic.Field( Logic.FieldType type) {
//        this.type=type;
//        ship=null;
//    }
    public void setShip(Ship ship){
        this.ship=ship;
        type= FieldType.WITHSHIP;
    }
    public FieldType getFieldType() {
        return type;
    }
    public void takeHit() {
        if (type == FieldType.WITHSHIP)
            ship.takeHit();
        type = FieldType.SHOTED;
    }
    public boolean isShipFloating() {
        if (ship==null || ship.getCondition()<=0)
           return false;
        else return true;
    }
    public void setSafetyZone(){
        type= FieldType.NEARSHIP;
    }
    public Point getBow(){
        return ship.getBow();
    }
    public int getLength() {
        return ship.getSize();
    }
    public Orientation getOrientaion() {
        return ship.getOrientation();
    }
}
