package Test;

import Controller.Controller;
import Model.Hexagon;
import View.GUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private Controller c;

    /**
     * Before each method to create a controller object c for each test case.
     * A controller object is required for each test case.
     */
    @BeforeEach
    void setUp() {
        c = new Controller();
    }

    /**
     * As per the project specification RED player should be the first
     * to move, as such this test ensures the RED player is the current player
     * when a game is initialised.
     */
    @Test
    void testStartGameRed() {
        assertEquals("RED", c.getCurrentPlayer());
    }

    /**
     * Tests the functionality of the switch turn function where
     * the controller will switch the current player when the switchTurn()
     * method is called. Testing from RED to BLUE and BLUE to RED.
     */
    @Test
    void testSwitchTurn() {
        // Switch BLUE
        c.switchTurn();
        assertEquals("BLUE", c.getCurrentPlayer());

        // Switching RED
        c.switchTurn();
        assertEquals("RED", c.getCurrentPlayer());
    }

    /**
     * Test to ensure a controller object creates a boardObject() which will
     * facilitate the running of the game. Asserts the getBoard() function returns
     * not null, asserts the returned object class is a Board.
     */
    @Test
    void testGetBoard() {
        assertNotNull(c.getBoard());
        assertEquals("Board", c.getBoard().getClass().getSimpleName());
    }

    /**
     * A test double implementation of the GUI class used to verify Controller-to-GUI
     * interactions in unit tests.
     *
     * This class extends the GUI and overrides the updateTurnIndicator() method
     * to track whether it has been called by the Controller. This implementation follows
     * the Humble Object Pattern to test interactions with the GUI without testing the
     * actual GUI implementation.
     */
    private static class DummyGUI extends GUI {
        boolean updateCalled = false;

        public DummyGUI(Controller c) {
            super(c);
        }
        @Override
        public void updateTurnIndicator() {
            updateCalled = true;
        }
    }

    /**
     * Test to ensure the controller calls the GUIs updateTurnIndicator() method.
     * Ensures that the GUI will get the signal to update the current player label
     * in game.
     *
     * Uses the DummyGUI Humble object to test this functionality. Switching the turn
     * in the controller should call the dummyGUI.updateTurnIndicator() which we can
     * make sure was called by checking updateCalled in the dummyGUI object.
     */
    @Test
    void testSwitchTurnUpdatesGUI() {
        DummyGUI dummyGUI = new DummyGUI(c);
        c.setGUI(dummyGUI);

        // Switch turn => should call dummyGUI.updateTurnIndicator()
        c.switchTurn();
        assertTrue(dummyGUI.updateCalled);
    }

    /**
     * Test to ensure handleMove() changes the current player from RED to BLUE.
     * currentPlayer is RED by Default
     */
    @Test
    void testHandleMoveSwitchesFromRedToBlue() {
        Hexagon h = new Hexagon(0,0, 0);

        c.handleMove(h);
        assertEquals("BLUE", c.getCurrentPlayer());
    }

    /**
     * After changing player from RED to BLUE. Tests to ensure BLUE to
     * RED player change functions correctly.
     */
    @Test
    void testHandleMoveSwitchesFromBlueToRed() {
        Hexagon h1 = new Hexagon(0,0, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);

        c.handleMove(h1);
        assertEquals("BLUE", c.getCurrentPlayer());
        c.handleMove(h2);
        assertEquals("RED", c.getCurrentPlayer());
    }

    @Test
    void testHandleMoveAssignsOwnerToHexagon() {
        Hexagon h1 = new Hexagon(0,0, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);

        // Assigns owners to hexagons
        c.handleMove(h1);
        assertEquals("RED", h1.getOwner());
        c.handleMove(h2);
        assertEquals("BLUE", h2.getOwner());
    }

    /**
     * Tests that when .handleMove() is given a hexagon, that hexagons owner is set
     * to the correct currentPlayer, specifically testing RED owner.
     */
    @Test
    void testHandleMoveAssignsRedOwnerToHexagon() {
        Hexagon h = new Hexagon(0,0, 0);

        assertEquals("RED", c.getCurrentPlayer());
        c.handleMove(h);
        assertEquals("RED", h.getOwner());
    }

    /**
     * Tests that when .handleMove() is given a hexagon, that hexagons owner is set
     * to the correct currentPlayer, specifically testing BLUE owner.
     */
    @Test
    void testHandleMoveAssignsBlueOwnerToHexagon() {
        Hexagon h1 = new Hexagon(0,0, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);

        c.handleMove(h1);

        assertEquals("BLUE", c.getCurrentPlayer());
        c.handleMove(h2);
        assertEquals("BLUE", h2.getOwner());
    }

    /**
     * Test to ensure duplicate moves are rejected and throw
     * an IllegalArgumentException when attempted.
     */
    @Test
    void testHandleMoveDuplicates() {
        Hexagon h = new Hexagon(0,0, 0);

        c.handleMove(h);
        assertThrows(IllegalArgumentException.class, () -> c.handleMove(h));
    }

    /**
     * Tests that passing a null value to null will throw a nullPointerException.
     */
    @Test
    void testHandleMoveNull() {
        assertThrows(NullPointerException.class, () -> c.handleMove(null));
    }

    /**
     * Test to ensure players cant place new hexagons next to pre-owned hexagons, when its
     * not a capturing move.
     */
    @Test
    void testInvalidPlacementAtAdjacentToOwnedHex() {
        // Using the hexagons generated by the board in c
        Hexagon h1 = c.getBoard().getHexagons().get(0); // q:-6, r:0, s:6
        Hexagon h2 = c.getBoard().getHexagons().get(1); // q:-6, r:1, s:5

        c.handleMove(h1);   // RED makes their move with h1
        c.switchTurn();     // Switch turn back to RED

        // Same player makes a move on a neighbouring hex, should throw an error
        assertThrows(IllegalArgumentException.class, () -> c.handleMove(h2) );   // RED makes move h2, neighbours h1
        assertNull(h2.getOwner()); // Ensure RED is not the owner of hexagon h2
    }

    /**
     * Testing of capturing move mechanics.
     * Tests that when a hexagon is captured, its owner is set back to null.
     */
    @Test
    void testCapturedHexagonHasOwnerSetToNull() {
        Hexagon h1 = c.getBoard().getHexagons().get(0); // q:-6, r:0, s:6
        Hexagon h2 = c.getBoard().getHexagons().get(1); // q:-6, r:1, s:5
        Hexagon h3 = c.getBoard().getHexagons().get(7); // q:-5, r:-1, s:6

        c.handleMove(h1); // RED places h1
        c.handleMove(h2); // BLUE places h2 adjacent to h1
        c.handleMove(h3); // RED places h3 adjacent to h1, [h1,h3] group of two adjacent to h2 -> capturing move

        assertNull(h2.getOwner());
    }

    /**
     * Testing of capturing move mechanics.
     * Tests that when a player plays a capturing move, their turn is retained.
     */
    @Test
    void testCapturingMoveRepeatsPlayerTurn() {
        Hexagon h1 = c.getBoard().getHexagons().get(0); // q:-6, r:0, s:6
        Hexagon h2 = c.getBoard().getHexagons().get(1); // q:-6, r:1, s:5
        Hexagon h3 = c.getBoard().getHexagons().get(7); // q:-5, r:-1, s:6

        c.handleMove(h1); // RED places h1
        c.handleMove(h2); // BLUE places h2 adjacent to h1

        assertEquals("RED", c.getCurrentPlayer()); // Assert RED turn before capturing move
        c.handleMove(h3); // RED places h3 adjacent to h1, [h1,h3] group of two adjacent to h2 -> capturing move

        assertEquals("RED", c.getCurrentPlayer()); // Assert RED turn after capturing move
    }

    /**
     * Helper function for running tests related to the noValidMoves() function.
     * Fills the board with the minumum number of RED hexagons such that there are
     * no moves remaining for the RED player.
     *
     * @param c The controller from the test case
     */
    private void blockAllValidMoves(Controller c) {
        // Array of positions to place hexagons into
        int[] hexPositions = {8, 10, 12, 14, 15, 34, 36,
                38, 40, 42, 44, 69, 70, 72,
                74, 76, 78, 80, 103, 105, 107,
                109, 111, 120, 122, 124, 126};

        ArrayList<Hexagon> hexagons = c.getBoard().getHexagons();

        // For loop which will add a RED hexagon in every position in hexPositions,
        // then switch the turn back to RED, to keep adding RED hexagons
        for (int i : hexPositions) {
            c.handleMove(hexagons.get(i));
            c.switchTurn();
        }
    }

    /**
     * Tests that hasValidMoves() will return false when current player
     * has no valid moves.
     */
    @Test
    void testHasValidMovesReturnsFalseWhenNoValidMoves() {
        blockAllValidMoves(c);

        assertEquals("RED", c.getCurrentPlayer()); // Check the current player is still RED
        assertFalse(c.hasValidMoves()); // Check that RED has no valid moves
    }

    /**
     * Tests that when a player attempts to make a move when they have no
     * valid moves, the hexagon they attempted to make a move on doesn't change
     * owners to current player.
     */
    @Test
    void testNoMovesMadeWhenNoValidMoves() {
        blockAllValidMoves(c);

        ArrayList<Hexagon> hexagons = c.getBoard().getHexagons();

        // Try to add a new hexagon at index 0, q:-6, r:0, s:6
        c.handleMove(hexagons.get(0));

        // Assert that the hexagon was not added by checking the owner at index 0
        assertNull(hexagons.get(0).getOwner());
    }

    /**
     * Tests that when a player has no valid moves and attempts to make a move,
     * their turn will be skipped.
     */
    @Test
    void testSkipTurnWhenNoValidMoves() {
        blockAllValidMoves(c);
        ArrayList<Hexagon> hexagons = c.getBoard().getHexagons();

        // Try to add a new hexagon at index 0, q:-6, r:0, s:6
        c.handleMove(hexagons.get(0));

        assertEquals("BLUE", c.getCurrentPlayer());
    }

    /**
     * Tests that the current player has valid moves when game starts.
     */
    @Test
    void testHasValidMovesOnGameStart() {
        assertTrue(c.hasValidMoves());
    }

    /**
     * Helper method to simulate a win occuring. Simulates the minimum amount
     * of moves required to win a game.
     * RED player wins after 3 moves, capturing BLUE players only tile.
     * @param c Takes input of the tests controller object
     */
    private void setupWinConditions(Controller c) {
        Hexagon h1 = c.getBoard().getHexagons().get(0); // q:-6, r:0, s:6
        Hexagon h2 = c.getBoard().getHexagons().get(2); // q:-6, r:2, s:4
        Hexagon h3 = c.getBoard().getHexagons().get(1); // q:-6, r:1, s:5

        // Win condition -> Capturing move + No opponent hexagons on the board
        c.handleMove(h1); // Red move
        c.handleMove(h2); // Blue move
        c.handleMove(h3); // Red move adjacent to blue, captures
    }

    /**
     * Tests that checkWin() returns the correct output when given
     * the winning player after win condition is met.
     */
    @Test
    void testCheckWinReturnsCorrectPlayer() {
        setupWinConditions(c);

        assertTrue(c.checkWin("RED"));
    }

    /**
     * Tests that the getGameOver() method returns True after win
     * condition is met.
     */
    @Test
    void testGetGameOverAfterWin() {
        setupWinConditions(c);

        assertTrue(c.getGameOver());
    }

    /**
     * Tests that on initialisation, the games GameOver state is false
     * to ensure the game can run.
     */
    @Test
    void testNewGameIsNotOverAtStart() {
        assertFalse(c.getGameOver());
    }
}