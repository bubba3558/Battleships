/**
 * Created by Martyna on 10.05.2017.
 */
public class Field {

    private FieldType type;
    private Ship ship;

    public Field() {
        type=FieldType.EMPTY;
        ship=null;
    }
    public Field( FieldType type) {
        this.type=type;
        ship=null;
    }
    public void setShip(Ship ship){
        this.ship=ship;
    }
    public FieldType getFieldType() {
        return type;
    }
    public void takeHit() {
        if (type == FieldType.WITHSHIP)
            ship.takeHit();
        type = FieldType.SHOTED;
}

}
