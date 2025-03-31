package Controller;

import Model.Board;
import Model.Hexagon;
import View.GUI;

public class Controller {
    private Board board;
    private String currentPlayer;
    private String gameState;
    private GUI gui;

    public Controller() {
        this.board = new Board();
        this.currentPlayer = "RED"; // Game starts with RED
        this.gameState = "Playing";
    }

    // Will be adding major changes to this method.
    // Future; check capture, check win.
    public void handleMove(Hexagon hex) {
        if (hex.getOwner() != null) {
            throw new IllegalArgumentException("Invalid Cell Placement -> " + hex);

        } else if (ownsNeighbor(hex)) {
            // Invalid, Neighbor owned by currentPLayer
            throw new IllegalArgumentException("Invalid Cell Placement -> " + hex);

        } else {
            // Valid hex placement
            hex.setOwner(currentPlayer);
            switchTurn();
        }
    }

    /**
     * Method to check if a Hexagons neighbors are owned by currentPlayer
     * @param hex
     * @return bool
     */
    public boolean ownsNeighbor(Hexagon hex) {
        // check 6 nearby Hex
        for (int i = 0; i < 6; i++) {
            Hexagon neighbor = hex.neighbor(i);

            // Search board for neighbor's coords
            for (Hexagon targetNeighbor : board.getHexagons()) {
                if (targetNeighbor.q == neighbor.q &&
                    targetNeighbor.r == neighbor.r &&
                    targetNeighbor.s == neighbor.s) {

                    // Invalid move, neighbor owned by currentPlayer
                    if (currentPlayer.equals(targetNeighbor.getOwner())) {
                        return true;
                    }
                    break; // Neighbor valid, check next neighbor
                }
            }
        }
        return false;
    }

    public void switchTurn() {
        // Switch between Players
        currentPlayer = currentPlayer.equals("RED") ? "BLUE" : "RED";

        // Change in GUI
        if (gui != null) {
            gui.updateTurnIndicator();
        }
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}