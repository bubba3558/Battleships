package battleships.gui;


import battleships.network.ErrorType;

public interface LoggingInterface {

    void startGame();

    void setError(ErrorType errorType);
}


