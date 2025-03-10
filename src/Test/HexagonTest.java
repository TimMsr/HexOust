package Test;

import Model.Hexagon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HexagonTest {

    @Test
    void validHexCreation() {
        Hexagon h = new Hexagon(1, -1, 0);
        // (q + r + s == 0)
        assertEquals(1, h.q);
        assertEquals(-1, h.r);
        assertEquals(0, h.s);
    }

    @Test
    void invalidHexCreation() {
        // (q + r + s != 0)
        assertThrows(IllegalArgumentException.class, () -> {
            new Hexagon(1, 1, 1);
        });
    }

    @Test
    void testAddition() {
        Hexagon h1 = new Hexagon(1, -1, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);
        Hexagon result = h1.add(h2);

        assertEquals(1, result.q);
        assertEquals(0, result.r);
        assertEquals(-1, result.s);
    }

    @Test
    void testSubtraction() {
        Hexagon h1 = new Hexagon(1, -1, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);
        Hexagon result = h1.subtract(h2);

        assertEquals(1, result.q);
        assertEquals(-2, result.r);
        assertEquals(1, result.s);
    }

    @Test
    void testLength() {
        // Length from origin : (q+r+s)/2 = l
        Hexagon h = new Hexagon(3, -2, -1);

        assertEquals(3, h.length());
    }

    @Test
    void testDistance() {
        // abs(h1-h2)/2 =
        Hexagon h1 = new Hexagon(4, 2, -6);
        Hexagon h2 = new Hexagon(2, -1, -1);

        assertEquals(5, h1.distance(h2));
    }

    // All directions 0-5 tested
    @Test
    void testDirection() {

        Hexagon d0 = Hexagon.direction(0);
        assertEquals(1, d0.q);
        assertEquals(0, d0.r);
        assertEquals(-1, d0.s);

        Hexagon d1 = Hexagon.direction(1);
        assertEquals(1, d1.q);
        assertEquals(-1, d1.r);
        assertEquals(0, d1.s);

        Hexagon d2 = Hexagon.direction(2);
        assertEquals(0, d2.q);
        assertEquals(-1, d2.r);
        assertEquals(1, d2.s);

        Hexagon d3 = Hexagon.direction(3);
        assertEquals(-1, d3.q);
        assertEquals(0, d3.r);
        assertEquals(1, d3.s);

        Hexagon d4 = Hexagon.direction(4);
        assertEquals(-1, d4.q);
        assertEquals(1, d4.r);
        assertEquals(0, d4.s);

        Hexagon d5 = Hexagon.direction(5);
        assertEquals(0, d5.q);
        assertEquals(1, d5.r);
        assertEquals(-1, d5.s);
    }

    // All 0-5 neighbouring directions tested form (0,0,0)
    @Test
    void testNeighbor() {
        Hexagon currHexagon = new Hexagon(0, 0, 0);

        Hexagon n0 = currHexagon.neighbor(0);
        assertEquals(1, n0.q);
        assertEquals(0, n0.r);
        assertEquals(-1, n0.s);

        Hexagon n1 = currHexagon.neighbor(1);
        assertEquals(1, n1.q);
        assertEquals(-1, n1.r);
        assertEquals(0, n1.s);

        Hexagon n2 = currHexagon.neighbor(2);
        assertEquals(0, n2.q);
        assertEquals(-1, n2.r);
        assertEquals(1, n2.s);

        Hexagon n3 = currHexagon.neighbor(3);
        assertEquals(-1, n3.q);
        assertEquals(0, n3.r);
        assertEquals(1, n3.s);

        Hexagon n4 = currHexagon.neighbor(4);
        assertEquals(-1, n4.q);
        assertEquals(1, n4.r);
        assertEquals(0, n4.s);

        Hexagon n5 = currHexagon.neighbor(5);
        assertEquals(0, n5.q);
        assertEquals(1, n5.r);
        assertEquals(-1, n5.s);
    }

    @Test
    void testToString() {
        Hexagon h1 = new Hexagon(1, -1, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);
        Hexagon h3 = new Hexagon(1, -1, 0);

        assertEquals("Hexagon[q:1, r:-1, s:0, owner:none]", h1.toString());

        h2.setOwner("RED");
        assertEquals("Hexagon[q:0, r:1, s:-1, owner:RED]", h2.toString());

        h3.setOwner("BLUE");
        assertEquals("Hexagon[q:1, r:-1, s:0, owner:BLUE]", h3.toString());
    }
}
