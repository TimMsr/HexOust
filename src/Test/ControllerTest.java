package Test;

import Controller.Controller;
import Model.Hexagon;
import View.GUI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    // Tests that when the controller is created the current player is RED
    @Test
    void testStartGameRed() {
        Controller c = new Controller();
        assertEquals("RED", c.getCurrentPlayer());
    }

    @Test
    void testSwitchTurn() {
        Controller c = new Controller();
        // Start RED
        assertEquals("RED", c.getCurrentPlayer());

        // Switch BLUE
        c.switchTurn();
        assertEquals("BLUE", c.getCurrentPlayer());

        // Switching RED
        c.switchTurn();
        assertEquals("RED", c.getCurrentPlayer());
    }

    @Test
    void testGetBoard() {
        Controller c = new Controller();
        assertNotNull(c.getBoard());
    }



    // test if updateTurnIndicator() is invoked
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
    @Test
    void testSwitchTurnUpdatesGUI() {
        Controller c = new Controller();
        DummyGUI dummyGUI = new DummyGUI(c);
        c.setGUI(dummyGUI);

        // Switch turn => should call dummyGUI.updateTurnIndicator()
        c.switchTurn();
        assertTrue(dummyGUI.updateCalled);
    }

    @Test
    void testHandleMoveSwitchesPlayers() {
        Controller c = new Controller();
        Hexagon h1 = new Hexagon(0,0, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);

        // HandleMove changes turn when valid move is made
        assertEquals("RED", c.getCurrentPlayer());
        c.handleMove(h1);
        assertEquals("BLUE", c.getCurrentPlayer());
        c.handleMove(h2);
        assertEquals("RED", c.getCurrentPlayer());
    }

    @Test
    void testHandleMoveAssignsOwnerToHexagon() {
        Controller c = new Controller();
        Hexagon h1 = new Hexagon(0,0, 0);
        Hexagon h2 = new Hexagon(0, 1, -1);

        // Assigns owners to hexagons
        c.handleMove(h1);
        assertEquals("RED", h1.getOwner());
        c.handleMove(h2);
        assertEquals("BLUE", h2.getOwner());
    }

    @Test
    void testHandleMoveDuplicates() {
        Controller c = new Controller();
        Hexagon h1 = new Hexagon(0,0, 0);

        // Passing a duplicate hexagon should throw an IllegalArgumentException
        c.handleMove(h1);
        assertThrows(IllegalArgumentException.class, () -> c.handleMove(h1));
    }

    @Test
    void testHandleMoveNull() {
        Controller c = new Controller();

        // Passing a null value should throw an NullPointerException
        //noinspection DataFlowIssue - suppression
        assertThrows(NullPointerException.class, () -> c.handleMove(null));
    }
}
