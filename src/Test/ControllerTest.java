package Test;

import Controller.Controller;
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

}