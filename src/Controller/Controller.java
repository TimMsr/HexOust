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
        if (hex.getOwner() == null) {
            hex.setOwner(currentPlayer);
            switchTurn();
        } else {
            throw new IllegalArgumentException("Invalid Cell Placement -> " + hex);
        }
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