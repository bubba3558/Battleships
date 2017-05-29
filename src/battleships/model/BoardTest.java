package test.board;

import model.Board;
import model.Orientation;
import model.Ship;
import exception.OutOfBoardException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    Board board;
    Ship ship1;
    Ship ship2;

    @Before
    public void initialize() {
        board = new Board(20, 20);
        ship1 = new Ship(2, Orientation.HORIZONTAL);
        ship2 = new Ship(5, Orientation.VERTICAL);
        try {
            board.placeShip(1, 1, ship1);
            board.placeShip(2, 2, ship2);
        } catch (Exception e)
            {}
    }

    @Test
    public void testIfHittingWorksCorrectly() {
        try {
            assertEquals("before hit ship should be healthly", ship1.getCondition(), 2);
            assertTrue(board.takeHit(0, 0));
            assertEquals("after hit ship should take damage", ship1.getCondition(), 1);
            assertTrue(board.takeHit(1, 0));
            assertEquals("after second hit ship should sunk", ship1.getCondition(), 0);
            assertTrue("after second hit ship should sunk", ship1.isDestroyed());
            assertFalse(board.takeHit(10, 10));
        } catch (Exception e)
            {}
    }

    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError0() throws OutOfBoardException {
        board.takeHit(-1, 0);
    }

    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError1() throws OutOfBoardException {
        board.takeHit(0, -1);
    }

    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError2() throws OutOfBoardException {
        board.takeHit(board.getWidth(), 0);
    }

    @Test(expected = OutOfBoardException.class)
    public void checkIfHittingOutsideBoardThrowsError3() throws OutOfBoardException {
        board.takeHit(0, board.getHeight() + 1);
    }

}
