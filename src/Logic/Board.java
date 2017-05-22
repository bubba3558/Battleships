package Logic;

import com.sun.org.apache.xpath.internal.operations.Bool;
import exception.CollisionException;
import exception.OutOfBoardException;

/**
 * Created by Martyna on 11.05.2017.
 */
public class Board {
    private Field[][] board;
    private int height;
    private int width;
    private FieldType Fie;

    public Board (){
        height=0;
        width=0;
        board=null;
    }
    public Board (int height, int width) {
        this.width=width;
        this.height=height;
        board=new Field[width+1][height+1];
        for( int i=1; i<=width; ++i)
            for ( int j=1; j<=height; j++)
                board[i][j]=new Field();
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public boolean placeShip(int startX, int startY, Ship ship) throws OutOfBoardException, CollisionException {
        if (ship.getOrientation()== Orientation.HORIZONTAL)
            return placeHorizontal(startX, startY, ship);
        else
            return placeVertical(startX, startY, ship);
    }
    private boolean placeHorizontal(int startX, int startY, Ship ship) throws OutOfBoardException, CollisionException {
        if(isPlaceableHorizontal(startX, startY, ship.getSize())) {
            for (int x = startX; x < startX + ship.getSize(); ++x) {
                board[x][startY].setShip(ship);
            }
            return true;
        }
        return false;
    }
    private boolean isPlaceableHorizontal(int startX, int startY, int shipSize)throws OutOfBoardException, CollisionException {
        for (int x=startX; x<startX+shipSize; ++x ) {
            if (!isFieldInsideBoard(x, startY))
                throw new OutOfBoardException("can not set ship outside the board");
            if (board[x][startY].getFieldType() == FieldType.WITHSHIP)
                throw new CollisionException("can not set two ships in the same place");
        }
        return true;
    }
    private boolean placeVertical(int startX, int startY, Ship ship) throws OutOfBoardException, CollisionException {
        if(isPlaceableVertical(startX,startY,ship.getSize())) {
            for (int y = startY; y < startY + ship.getSize(); ++y) {
                board[startX][y].setShip(ship);
            }
            return true;
        }
        return false;
     }

    private boolean isPlaceableVertical(int startX, int startY, int shipSize)throws OutOfBoardException, CollisionException {
        for (int y=startY; y<startY+shipSize; ++y ){
            if( !isFieldInsideBoard( startX,y) )
                throw new OutOfBoardException("can not set ship outside the board");
            if(board[startX][y].getFieldType()==FieldType.WITHSHIP)
                throw new CollisionException("can not set two ships in the same place");
        }
        return true;
    }


    public boolean takeHit(int x, int y) throws OutOfBoardException{
        if (!isFieldInsideBoard(x, y))
            throw new OutOfBoardException("can not choose field outside the board");
        if(board[x][y].getFieldType()== FieldType.WITHSHIP) {
            board[x][y].takeHit();
            return true;
        }
        board[x][y].takeHit();
        return false;
    }

    private boolean isFieldInsideBoard(int x, int y){
        if ( y<=0 || x<=0 || y>height || x>width )
            return false;
        return true;
    }

    public FieldType getFieldType(int x, int y){
        return board[x][y].getFieldType();
    }

    public Boolean isShipFloating(int x, int y) {
        return board[x][y].isShipFloating();
    }
}
