package test.java;

import Logic.Board;
import Logic.Orientation;
import Logic.Ship;
import Logic.FieldType;
import exception.OutOfBoardException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Martyna on 15.05.2017.
 */
@RunWith(Arquillian.class)
public class BoardTest {

    Board board;
    Ship ship1;
    Ship ship2;
    public void setUp0() {
        board = new Board(20, 20);
        ship1 = new Ship(2, Orientation.HORIZONTAL);
        ship2 = new Ship(5, Orientation.VERTICAL);
        }
    public void setUp1() {
        setUp0();
        try{
            board.placeShip(1, 1, ship1);
            board.placeShip(2, 2, ship2);}
            catch (Exception e){
            }
        }
    @Test
    public void testFieldsWorksCorrectly(){
        try {
            setUp0();
            assertEquals("Before placement a ship field type = empty",board.getFieldType(0,0), FieldType.EMPTY);
            assertEquals("Before placement a ship field type = empty",board.getFieldType(0,1), FieldType.EMPTY);
            board.placeShip(0, 0, ship1);
            assertEquals("After placement a ship field type = ship",board.getFieldType(0,0), FieldType.WITHSHIP);
            board.takeHit(0,0);
            assertEquals("After shooting a field type= shooted = ship",board.getFieldType(0,0), FieldType.SHOTED);
            board.takeHit(0,1);
            assertEquals("After shooting a field type= shooted = ship",board.getFieldType(0,0), FieldType.SHOTED);
        }catch (Exception e){
        }
    }
    @Test
    public void testIfHittingWorksCorrectly(){
        try {
            setUp1();
            assertEquals("before hit ship should be healthly", ship1.getCondition(), 2);
            assertTrue(board.takeHit(0, 0) );
            assertEquals("after hit ship should take damage", ship1.getCondition(), 1);
            assertTrue(board.takeHit(1, 0) );
            assertEquals("after second hit ship should sunk", ship1.getCondition(), 0);
            assertTrue("after second hit ship should sunk", ship1.isDestroyed());
            assertFalse(board.takeHit(10, 10) );
        }catch (Exception e){
        }
    }
    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError0() throws OutOfBoardException {
        setUp1();
        board.takeHit(-1, 0);
    }
    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError1() throws OutOfBoardException {
        setUp1();
        board.takeHit(0 , -1);
    }
    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError2() throws OutOfBoardException {
        setUp1();
        board.takeHit(board.getWidth(), 0);
    }
    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError3() throws OutOfBoardException {
        setUp1();
        board.takeHit(0, board.getHeight()+1);
    }

}
