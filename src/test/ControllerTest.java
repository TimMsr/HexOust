package test;

import Controller.Controller;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    // Tests that when the controller is created the current player is RED
    @Test
    void testStartGameRed() {
        Controller controller = new Controller();

        assertEquals("RED", controller.getCurrentPlayer());
    }

    // Tests functionality of switchTurn()
    @Test
    void testSwitchTurn() {
        Controller controller = new Controller();

        assertEquals("RED", controller.getCurrentPlayer()); // Test the current player is RED first
        controller.switchTurn();
        assertEquals("BLUE", controller.getCurrentPlayer()); // After switchTurn(), player is BLUE
        controller.switchTurn();
        assertEquals("RED", controller.getCurrentPlayer()); // switchTurn() cycles back to RED
    }
}