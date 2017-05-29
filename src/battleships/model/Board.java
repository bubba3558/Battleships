package model;

import exception.CollisionException;
import exception.OutOfBoardException;


public class Board {
    private Field[][] board;
    private int height;
    private int width;

    /**
     * add extra row to optimize surrounding ships with fields
     */
    public Board(int height, int width) {
        this.width = width;
        this.height = height;
        board = new Field[width + 2][height + 2];
        for (int i = 0; i <= width + 1; ++i) {
            for (int j = 0; j <= height + 1; j++) {
                board[i][j] = new Field();
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean placeShip(int startX, int startY, Ship ship) throws OutOfBoardException, CollisionException {
        if (ship.getOrientation() == Orientation.HORIZONTAL) {
            return placeHorizontal(startX, startY, ship);
        } else {
            return placeVertical(startX, startY, ship);
        }
    }

    private boolean placeHorizontal(int startX, int startY, Ship ship) throws OutOfBoardException, CollisionException {
        if (isPlaceableHorizontal(startX, startY, ship.getSize())) {
            board[startX - 1][startY - 1].setSafetyZone();
            board[startX - 1][startY].setSafetyZone();
            board[startX - 1][startY + 1].setSafetyZone();
            for (int x = startX; x < startX + ship.getSize(); ++x) {
                board[x][startY - 1].setSafetyZone();
                board[x][startY].setShip(ship);
                board[x][startY + 1].setSafetyZone();
            }
            board[startX + ship.getSize()][startY - 1].setSafetyZone();
            board[startX + ship.getSize()][startY].setSafetyZone();
            board[startX + ship.getSize()][startY + 1].setSafetyZone();
            return true;
        }
        return false;
    }

    private boolean isPlaceableHorizontal(int startX, int startY, int shipSize) throws OutOfBoardException, CollisionException {
        for (int x = startX; x < startX + shipSize; ++x) {
            checkIfShipCanBePlacedInField(x, startY);
        }
        return true;
    }

    private boolean placeVertical(int startX, int startY, Ship ship) throws OutOfBoardException, CollisionException {
        if (isPlaceableVertical(startX, startY, ship.getSize())) {
            board[startX - 1][startY - 1].setSafetyZone();
            board[startX][startY - 1].setSafetyZone();
            board[startX + 1][startY - 1].setSafetyZone();
            for (int y = startY; y < startY + ship.getSize(); ++y) {
                board[startX - 1][y].setSafetyZone();
                board[startX][y].setShip(ship);
                board[startX + 1][y].setSafetyZone();
            }
            board[startX - 1][startY + ship.getSize()].setSafetyZone();
            board[startX][startY + ship.getSize()].setSafetyZone();
            board[startX + 1][startY + ship.getSize()].setSafetyZone();
            return true;
        }
        return false;
    }

    private boolean isPlaceableVertical(int startX, int startY, int shipSize) throws OutOfBoardException, CollisionException {
        for (int y = startY; y < startY + shipSize; ++y) {
            checkIfShipCanBePlacedInField(startX, y);
        }
        return true;
    }

    /**
     * throws exception if not
     */
    private void checkIfShipCanBePlacedInField(int x, int y) throws OutOfBoardException, CollisionException {
        if (!isFieldInsideBoard(x, y)) {
            throw new OutOfBoardException("can not set ship outside the board");
        }
        if (board[x][y].getFieldType() == FieldType.WITHSHIP) {
            throw new CollisionException("can not set two ships in the same place");
        }
        if (board[x][y].getFieldType() == FieldType.NEARSHIP) {
            throw new CollisionException("can not set two ships near each other");
        }
    }

    public boolean takeHit(int x, int y) throws OutOfBoardException {
        if (!isFieldInsideBoard(x, y)) {
            throw new OutOfBoardException("can not choose field outside the board");
        }
        if (board[x][y].getFieldType() == FieldType.WITHSHIP) {
            board[x][y].takeHit();
            return true;
        }
        board[x][y].takeHit();
        return false;
    }

    public boolean isFieldInsideBoard(int x, int y) {
        return !(y <= 0 || x <= 0 || y > height || x > width);
    }

    public FieldType getFieldType(int x, int y) {
        return board[x][y].getFieldType();
    }

    public Boolean isShipFloating(int x, int y) {
        return board[x][y].isShipFloating();
    }

    public int getShipLength(int x, int y) {
        return board[x][y].getLength();
    }

    public Point getBow(int x, int y) {
        return board[x][y].getBow();
    }

    public Orientation getOrientation(int x, int y) {
        return board[x][y].getOrientaion();
    }
}
