package gui;

import javafx.scene.layout.Pane;

public class Cell extends Pane {
    public final int x;
    public final int y;

    public static final String WATERCOLOR = "#5b7cea";

    protected boolean wasShoot;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        wasShoot = false;
        setColor(WATERCOLOR);
    }

    public void setColor(String color) {
        this.setStyle("-fx-background-color: " + color);
    }

    public void resetColor() {
        setColor(WATERCOLOR);
    }

    public void setShoot() {
        wasShoot = true;
    }

    public boolean wasShoot() {
        return wasShoot;
    }

    public void reset() {
        wasShoot = false;
        setColor(WATERCOLOR);;
    }
}
