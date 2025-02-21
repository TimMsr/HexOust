package Controller;

import Model.Board;
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