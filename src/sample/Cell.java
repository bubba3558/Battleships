package sample;

import javafx.scene.layout.Pane;

/**
 * Created by Martyna on 19.05.2017.
 */
public class Cell extends Pane {
    public static int x;
    public static int y;

    private boolean wasShooted;
    public Cell(int x, int y){
        this.x=x;
        this.y=y;
        wasShooted=false;
        this.setStyle("-fx-background-color: " + "#5b7cea");
    }
    public void setShoot(){
        wasShooted=true;
    }
    public boolean wasShooted(){
        return wasShooted;
    }
}
