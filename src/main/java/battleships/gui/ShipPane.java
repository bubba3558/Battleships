package battleships.gui;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ShipPane extends HBox{
    public int length;
    public ShipPane(int length){
        setSpacing(2);
        this.length=length;
        for (int i = 0; i < length; ++i) {
            Rectangle blueRectangle = new Rectangle(30, 30);
            blueRectangle.setFill(Color.web("#1e90ff"));
            getChildren().add(blueRectangle);
        }
    }

    public void setPlaced(){
        setDisable(true);
        setOpacity(0);
    }

    public void reset(){
        setOpacity(1);
        setDisable(false);
    }
    public int getLength(){
        return length;
    }

}
