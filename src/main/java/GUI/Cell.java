package GUI;

import javafx.scene.layout.Pane;

/**
 * Created by Martyna on 19.05.2017.
 */
public class Cell extends Pane {
    public final int x;
    public final int y;

    protected boolean wasShoot;
    public Cell(int x, int y){
        this.x=x;
        this.y=y;
        wasShoot=false;
        this.setStyle("-fx-background-color: #5b7cea");
    }
    public Cell(int x, int y, boolean setColor){
        this.x=x;
        this.y=y;
        wasShoot=false;
        if(setColor)
            this.setStyle("-fx-background-color: #5b7cea");
    }
    public void setShoot(){
        wasShoot=true;
    }
    public boolean wasShoot(){
        return wasShoot;
    }
    public void reset(){
        wasShoot=false;
        this.setStyle("-fx-background-color: #5b7cea");
    }
}
