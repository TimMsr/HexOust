package Test;

import Model.Board;
import Model.Hexagon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BoardTest {

    /**
     * Tests that the board size is 127 hexagons.
     * Necessary for the functioning of the current game format.
     */
    @Test
    void testCorrectBoardSize(){
        Board board = new Board();
        ArrayList<Hexagon> hexagons = board.getHexagons();
        assertEquals(127, hexagons.size());
    }

    /**
     * Tests that all the hexagons created by the board
     * are valid hexagons, each hexagon not returning
     * null and satisfying the "q + r + s == 0" rule.
     */
    @Test
    void testGetHexagonsReturnsValidHexagons() {
        Board board = new Board();
        ArrayList<Hexagon> hexagons = board.getHexagons();
        assertNotNull(hexagons);

        for (Hexagon h : hexagons) {
            assertNotNull(h);
            assertEquals(0, h.q + h.r + h.s);
        }
    }

    /**
     * Tests that the origin hexagon is created and can
     * be found inside the hexagons list on the board.
     */
    @Test
    void testBoardContainsCenterHexagon() {
        Board board = new Board();
        ArrayList<Hexagon> hexagons = board.getHexagons();
        boolean centerFound = false;

        for (Hexagon h : hexagons) {
            if (h.q == 0 && h.r == 0 && h.s == 0) {
                centerFound = true;
                break;
            }
        }
        assertTrue(centerFound);
    }

    /**
     * Test to ensure the board doesn't contain any hexagons
     * with duplicate coordinates. Duplicate coordinates would
     * lead to fundamental issues with the running of the game.
     */
    @Test
    void testBoardHasNoDuplicateHexagons() {
        Board board = new Board();
        ArrayList<Hexagon> hexagons = board.getHexagons();
        Set<String> coordinateSet = new HashSet<>();

        for (Hexagon h : hexagons) {
            // check set, unique coords as key
            String key = h.q + "," + h.r+ "," + h.s;

            assertFalse(coordinateSet.contains(key), "Found duplicate: " + key);
            coordinateSet.add(key);
        }
    }

    /**
     * Tests the getHexagonAt() function which takes the input of x, y coordinates.
     * The function works by taking the input position of the users mouse to assess
     * what hexagon is available at that position.
     *
     * Tests that getHexagonAt() with the x, y, input of the center position of the board
     * will return an instance of the hexagon class.
     */
    @Test
    void testGetHexagonAtCenterCoordinates() {
        Board board = new Board();
        // The board is centered at 375, 375 with a hex size of 30

        // Center hex at x:375, y:375, should return a hexagon
        Hexagon h = board.getHexagonAt(375, 375);
        assertInstanceOf(Hexagon.class, h);
    }

    /**
     * Tests that when the input of getHexagonAt() is outside the board
     * the method won't return any object (null).
     */
    @Test
    void testGetHexagonAtCoordinatesOutsideBoard() {
        Board board = new Board();

        // Origin is outside of board, therefore no hexagon should be returned at 0, 0
        Hexagon h = board.getHexagonAt(0, 0);
        assertNull(h);
    }
}
