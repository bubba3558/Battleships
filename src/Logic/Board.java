package Logic;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
        board=new Field[width][height];
        for( int i=0; i<width; ++i)
            for ( int j=0; j<height; j++)
                board[i][j]=new Field();
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public void placeShip(int startX, int startY, Ship ship) throws OutOfBoardException{
        if (ship.getOrientation()== Orientation.HORIZONTAL)
            placeHorizontal(startX, startY, ship);
        if (ship.getOrientation()== Orientation.VERTICAL)
            placeVertical(startX, startY, ship);
    }
    private void placeHorizontal(int startX, int startY, Ship ship)throws OutOfBoardException{
        for (int x=startX; x<startX+ship.getSize(); ++x ) {
            if( !isFieldInsideBoard( x, startY) )
                throw new OutOfBoardException("can not set ship outside the board");
            board[x][startY].setShip(ship);
        }
    }
    private void placeVertical(int startX, int startY, Ship ship) throws OutOfBoardException{

        for (int y=startY; y<startY+ship.getSize(); ++y ) {
            if( !isFieldInsideBoard( startX,y) )
                throw new OutOfBoardException("can not set ship outside the board");
            board[startX][y].setShip(ship);
        }
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
        if ( y<0 || x<0 || y>=height || x>=width )
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
