package sample;

import javafx.scene.layout.Pane;

/**
 * Created by Martyna on 19.05.2017.
 */
public class Cell extends Pane {
    public static int x;
    public static int y;
    boolean wasShoted;
    public Cell(int x, int y){
        this.x=x;
        this.y=y;
        wasShoted=false;
    }
}
