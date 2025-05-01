package View;

import Controller.Controller;
import Model.Hexagon;

import javax.swing.*;
import java.util.List;

public final class FilledBoardDemo {

    public static void main(String[] args) {
        // From Hubert's testing:

        // create controller and fill the board
        Controller c = new Controller();

        int[] hexPositions = { 8, 10, 12, 14, 15, 34, 36,
                38, 40, 42, 44, 69, 70, 72,
                74, 76, 78, 80, 103, 105, 107,
                109, 111, 120, 122, 124, 126 };

        List<Hexagon> hexagons = c.getBoard().getHexagons();

        // Set player "BLUE" or "RED" - Capitalize!
        c.setCurrentPlayer("BLUE");
        for (int idx : hexPositions) {
            c.handleMove(hexagons.get(idx)); // place Hex
            c.switchTurn(); // switch back so that same player moves
        }

        // launch the GUI on the EDT (invokeLater)
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(c);
            c.setGUI(gui); // let controller talk to the view
            gui.start();
        });
    }
}