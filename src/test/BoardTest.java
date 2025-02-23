// package Board;
package test;
import Model.Board;
import Model.Hexagon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    // Test to ensure the board remains the right size whenever initialized
    @Test
    void boardSize_Returns_127() {
        Board board = new Board();

        assertEquals(127, board.getHexagons().size());
    }

    @Test
    void getHexagons_ReturnsArrayListOfHexagons() {
        Board board = new Board();

        ArrayList<Hexagon> hexagons = board.getHexagons();

        assertNotNull(hexagons); // Assert the arrayList is not null

        for (Object obj : hexagons) {
            assertInstanceOf(Hexagon.class, obj); // Make sure every element in the list is a Hexagon
        }
    }
}