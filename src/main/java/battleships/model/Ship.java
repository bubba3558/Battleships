package battleships.model;

public class Ship {
    private int size;
    private int condition;
    private Orientation orientation;
    private Point bow = null;


    public Ship(int size, Orientation orientation) {
        this.size = size;
        this.orientation = orientation;
        this.condition = size;
    }

    public Ship(int size, Orientation orientation, Point bow) {
        this.size = size;
        this.orientation = orientation;
        this.condition = size;
        this.bow = bow;
    }

    public void setBow(Point bow) {
        this.bow = bow;
    }

    public Point getBow() {
        return bow;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getCondition() {
        return condition;
    }

    public int getSize() {
        return size;
    }

    public Boolean isDestroyed() {
        return condition == 0;
    }

    public void takeHit() {
        --condition;
    }
}
