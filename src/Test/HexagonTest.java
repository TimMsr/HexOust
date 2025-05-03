package Test;

import Model.Hexagon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HexagonTest {

    /**
     * Test to verify that all coordinates of a Hexagon are set correctly.
     * - Needs to satisfy "q + r + s == 0" rule.
     */
    @Test
    void testNewHexagonCorrectAllValues() {
        Hexagon h = new Hexagon(1, -1, 0);
        // (q + r + s == 0)
        assertEquals(1, h.q);
        assertEquals(-1, h.r);
        assertEquals(0, h.s);
    }

    /**
     * Tests that a created Hexagon maintains the "q + r + s == 0" rule.
     */
    @Test
    void testHexagonCoordsSumZero() {
        Hexagon h = new Hexagon(1, -1, 0);
        assertEquals(0, h.q + h.r + h.s);
    }

    /**
     * Tests that creating a hexagon that does not satisfy "q + r + s == 0" rule will throw an error.
     */
    @Test
    void invalidHexCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Hexagon(1, 1, 1));
    }

    /**
     * Testing hexagon addition functioning where new hexagons will return expected values
     */
    @Test
    void testHexagonAdditionReturnsCorrectValues() {
        Hexagon h1 = new Hexagon(1, -1, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);
        Hexagon result = h1.add(h2);

        assertEquals(1, result.q);
        assertEquals(0, result.r);
        assertEquals(-1, result.s);
    }

    /**
     * Tests that the "q + r + s == 0" rule is maintained in
     * hexagons which have been created as a result of addition.
     */
    @Test
    void testHexagonAdditionMaintainsZeroSumRule() {
        Hexagon h1 = new Hexagon(1, -1, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);
        Hexagon result = h1.add(h2);

        assertEquals(0, result.q + result.r + result.s);
    }

    /**
     * Tests that the hexagon subtraction method returns a hexagon
     * with the correct values for each coordinate
     */
    @Test
    void testHexagonSubtractionReturnsCorrectValues() {
        Hexagon h1 = new Hexagon(1, -1, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);
        Hexagon result = h1.subtract(h2);

        assertEquals(1, result.q);
        assertEquals(-2, result.r);
        assertEquals(1, result.s);
    }

    /**
     * Test to ensure hexagons created after hexagon subtraction
     * maintain the "q + r + s == 0" rule.
     */
    @Test
    void testSubtractionMaintainsZeroSumRule() {
        Hexagon h1 = new Hexagon(1, -1, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);
        Hexagon result = h1.subtract(h2);

        assertEquals(0, result.q + result.r + result.s);
    }

    /**
     * Tests the functioning of the hexagon.length function.
     * Correct functioning should return the length from the origin from the formula:
     * (q + r + s) / 2 = length, where q,r,s, are all absolute values
     */
    @Test
    void testHexagonLengthFromOrigin() {
        Hexagon h = new Hexagon(3, -2, -1);

        assertEquals(3, h.length());
    }

    /**
     * Test to ensure the distance between two hexagons is calculated correctly.
     * Distance is found by adding the difference between each q,r,s coordinate
     * in both hexagons and dividing the result by 2.
     */
    @Test
    void testHexagonDistanceReturnsCorrectValue() {
        Hexagon h1 = new Hexagon(4, 2, -6);
        Hexagon h2 = new Hexagon(2, -1, -1);

        assertEquals(5, h1.distance(h2));
    }

    // All directions 0-5 tested

    /**
     * Hexagon.direction() returns the movement direction corresponding to the given index.
     * The index must be between 0 and 5, representing the 6 directions
     * E.g. Hexagon newHex = currentHex.add(Hexagon.direction(0)); - Moves right
     *
     * We created a helper method as the code is the same for each direction just with different parameters.
     *
     * These 6 test cases test the functionality of each direction, split into different
     * tests for each direction to ensure if one direction fails rest will still be tested.
     */

    /**
     * Hexagon.direction() returns the movement direction corresponding to the given index.
     * E.g. Hexagon newHex = currentHex.add(Hexagon.direction(0)); - Moves right
     *
     * We created a helper method as the code is the same for each direction just with different parameters.
     *
     * These 6 test cases test the functionality of each direction, split into different
     * tests for each direction to ensure if one direction fails rest will still be tested.
     *
     * @param direction The index between 0 and 5, representing the 6 directions
     * @param q Expect q value for the hexagon at the direction
     * @param r Expect r value for the hexagon at the direction
     * @param s Expect s value for the hexagon at the direction
     */
    private void assertHexagonDirection(int direction, int q, int r, int s) {
        Hexagon h = Hexagon.direction(direction);
        assertEquals(q, h.q);
        assertEquals(r, h.r);
        assertEquals(s, h.s);
    }

    @Test
    void TestHexagonDirection0() {
        assertHexagonDirection(0, 1, 0, -1);
    }

    @Test
    void TestHexagonDirection1() {
        assertHexagonDirection(1, 1, -1, 0);
    }

    @Test
    void TestHexagonDirection2() {
        assertHexagonDirection(2, 0, -1, 1);
    }

    @Test
    void TestHexagonDirection3() {
        assertHexagonDirection(3, -1, 0, 1);
    }

    @Test
    void TestHexagonDirection4() {
        assertHexagonDirection(4, -1, 1, 0);
    }

    @Test
    void TestHexagonDirection5() {
        assertHexagonDirection(5, 0, 1, -1);
    }

    /**
     * Helper method to assert testing of the neighbour method.
     * Works by calculating the neighbour in a chosen direction from
     * the origin, then checking that the coordinates match those of
     * the neighbour in that direction.
     *
     * @param direction The direction from the origin hexagon to get neighbour from
     * @param q The expected q coordinate of the neighbour
     * @param r The expected r coordinate of the neighbour
     * @param s The expected s coordinate of the neighbour
     */
    private void assertNeighbourFromOrigin(int direction, int q, int r, int s) {
        Hexagon currHexagon = new Hexagon(0, 0, 0);

        Hexagon n = currHexagon.neighbor(direction);
        assertEquals(q, n.q);
        assertEquals(r, n.r);
        assertEquals(s, n.s);
    }

    /**
     * Tests each neighbour in each direction (0 - 5) matches expected coordinates
     * uses assertNeighbourFromOrigin helper method to reduce code repetition.
     */
    @Test
    void testNeighbourDirection0() {
        assertNeighbourFromOrigin(0, 1, 0, -1);
    }

    @Test
    void testNeighbourDirection1() {
        assertNeighbourFromOrigin(1, 1, -1, 0);
    }

    @Test
    void testNeighbourDirection2() {
        assertNeighbourFromOrigin(2, 0, -1, 1);
    }

    @Test
    void testNeighbourDirection3() {
        assertNeighbourFromOrigin(3, -1, 0, 1);
    }

    @Test
    void testNeighbourDirection4() {
        assertNeighbourFromOrigin(4, -1, 1, 0);
    }

    @Test
    void testNeighbourDirection5() {
        assertNeighbourFromOrigin(5, 0, 1, -1);
    }

    /**
     * Test to ensure that a hexagon with no defined owner returns
     * a string with the owner as NULL
     */
    @Test
    void testToStringNullOwner() {
        Hexagon h = new Hexagon(0, 0, 0);

        assertEquals("Hexagon[q:0, r:0, s:0, owner:NULL]", h.toString());
    }

    /**
     * Test to ensure setting the owner of a hexagon as RED returns
     * correct string format.
     */
    @Test
    void testToStringRedOwner() {
        Hexagon h = new Hexagon(0, 0, 0);

        h.setOwner("RED");
        assertEquals("Hexagon[q:0, r:0, s:0, owner:RED]", h.toString());
    }

    /**
     * Test to ensure setting the owner of a hexagon as BLUE returns
     * correct string format.
     */
    @Test
    void testToStringBlueOwner() {
        Hexagon h = new Hexagon(0, 0, 0);

        h.setOwner("BLUE");
        assertEquals("Hexagon[q:0, r:0, s:0, owner:BLUE]", h.toString());
    }

    /**
     * Tests that getOwner() returns the correct owner after being set.
     */
    @Test
    void testGetOwnerReturnsCorrectValue() {
        Hexagon h = new Hexagon(0, 0, 0);

        h.setOwner("RED");
        assertEquals("RED", h.getOwner());
    }

    /**
     * Tests that getOwner() returns NULL before being set.
     */
    @Test
    void testGetOwnerReturnsNullValue() {
        Hexagon h = new Hexagon(0, 0, 0);

        assertNull(h.getOwner());
    }
}
