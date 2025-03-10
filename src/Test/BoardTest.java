package Test;

import Model.Board;
import Model.Hexagon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BoardTest {

    @Test
    void boardSize(){
        Board board = new Board();
        ArrayList<Hexagon> hexagons = board.getHexagons();
        assertEquals(127, hexagons.size());
    }

    @Test
    void getHexagons() {
        Board board = new Board();
        ArrayList<Hexagon> hexagons = board.getHexagons();
        assertNotNull(hexagons); // list should have hexagons

        for (Hexagon h : hexagons) {
            // hexagons should have properties
            assertNotNull(h);
            // Board should create valid hexagons (q+r+s == 0)
            assertEquals(0, h.q + h.r + h.s);
        }
    }

    // Check for origin hexagon (0,0,0)
    @Test
    void boardContainsCenterHexagon() {
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

    // No hexagons with dupe coords
    @Test
    void noDuplicateHexagons() {
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

    @Test
    void testGetHexagonAt() {
        Board board = new Board();
        // The board is centered at 375, 375 with a hex size of 30

        // Center hex at x:375, y:375, should return a hexagon
        Hexagon h1 = board.getHexagonAt(375, 375);
        assertInstanceOf(Hexagon.class, h1);

        // Origin is outside of board, therefore no hexagon should be returned at 0, 0
        Hexagon h2 = board.getHexagonAt(0, 0);
        assertNull(h2);
    }
}
