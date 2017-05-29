package gui;

import model.GameMessageType;
import model.Orientation;

public interface GameControllerInterface {

    void setWin();

    void setLost();

    void restartView();

    void setMessage(GameMessageType messageType);

    void setMiss(int x, int y);

    void setOpponentMiss(int x, int y);

    void setShipHit(int x, int y);

    void setYourShipHit(int x, int y);

    void setShipSunkHit(int x, int y, int length, Orientation orientation);

    void setYourShipSunk(int x, int y, int length, Orientation orientation);

    void setLostConnection();

}
