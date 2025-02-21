package Controller;

import Model.board;
import Model.Hexagon;
import View.GUI;

public class Controller {
    private Board board;
    private String currentPlayer;
    private String gameState;

    public Controller() {
        board = new Board();
        currentPlayer = "Red"; // Default starting player
        gameState = "Playing";
    }

    public void startGame() {
        System.out.println("Game Started!");
    }

    public void switchTurn() {
        currentPlayer = currentPlayer.equals("Red") ? "Blue" : "Red";
    }

    public boolean checkWin() {
        return false; // Implement win condition logic later
    }

    public Board getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
