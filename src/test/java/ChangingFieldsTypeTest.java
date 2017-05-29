import model.FieldType;
import org.junit.Before;
import org.junit.Test;
import model.Board;
import model.Orientation;
import model.Ship;

import static org.junit.Assert.assertEquals;

public class ChangingFieldsTypeTest {

    Board board;
    Ship ship1;
    Ship ship2;

    @Before
    public void initialize() {
        board = new Board(20, 20);
        ship1 = new Ship(2, Orientation.HORIZONTAL);
        ship2 = new Ship(5, Orientation.VERTICAL);
    }

    @Test
    public void testFieldsWorksCorrectly() {
        try {
            assertEquals("Before placement a ship field type = empty", board.getFieldType(0, 0), FieldType.EMPTY);
            assertEquals("Before placement a ship field type = empty", board.getFieldType(0, 1), FieldType.EMPTY);
            board.placeShip(0, 0, ship1);
            assertEquals("After placement a ship field type = ship", board.getFieldType(0, 0), FieldType.WITHSHIP);
            board.takeHit(0, 0);
            assertEquals("After shooting a field type= shooted = ship", board.getFieldType(0, 0), FieldType.SHOTED);
            board.takeHit(0, 1);
            assertEquals("After shooting a field type= shooted = ship", board.getFieldType(0, 0), FieldType.SHOTED);
        } catch (Exception e) {
        }
    }
}
