package Controller;

import Model.Board;
import Model.Hexagon;
import View.GUI;

import java.util.*;

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

    /**
     * Handles currentPlayer moves
     * @param hex The hexagon to place on the board
     */
    public void handleMove(Hexagon hex) {
        if (hex.getOwner() != null) {
            throw new IllegalArgumentException("Invalid Cell Placement -> " + hex);
        }
        // Look for hexagons to capture, return true if captured hexagons are erased
        boolean capMove = eraseHexagons((captureMove(hex)));

        if (ownsNeighbor(hex) && !capMove) {
            // If the hex is adjacent to one of the current player's hexagons but does not create a capture,
            // the move is invalid.
            throw new IllegalArgumentException("Invalid Cell Placement -> " + hex);

        } else {
            // Valid hex placement
            hex.setOwner(currentPlayer);

            if (!capMove) {
                switchTurn();
            }
            // Capture move testing in terminal
            System.out.println("CP Move: " + capMove);
        }
    }

    /**
     * Checks if any neighbor of the given hexagon is owned by the current player.
     * @param hex The hexagon to check around.
     * @return true if at least one neighbor is owned by the current player, false otherwise.
     */
    public boolean ownsNeighbor(Hexagon hex) {
        // Check 6 neighboring hexagons
        for (int i = 0; i < 6; i++) {
            Hexagon neighbor = hex.neighbor(i);
            // Search board for the hex with matching coordinates
            for (Hexagon targetNeighbor : board.getHexagons()) {
                if (targetNeighbor.q == neighbor.q &&
                    targetNeighbor.r == neighbor.r &&
                    targetNeighbor.s == neighbor.s) {

                    // Return true if the neighbor is owned by the current player
                    if (currentPlayer.equals(targetNeighbor.getOwner())) {
                        return true;
                    }
                    break; // Found the neighbor; proceed to next direction
                }
            }
        }
        return false;
    }


    /**
     * Compares nearby grouped hexagons and their owners to check for a capture move.
     * @param placedHex The hexagon that may invoke a capture move
     * @return HashSet of all capture hexagons to erase.
     */
    public HashSet<Hexagon> captureMove(Hexagon placedHex) {
        // todo
        // Group of captured hexagons to return
        HashSet<Hexagon> capturedSet = new HashSet<>();

        // Simulate placing hex by currentPlayer.
        placedHex.setOwner(currentPlayer);
        // Compute the connected group of currentPlayer.
        HashSet<Hexagon> currentGroup = getConnectedGroup(placedHex, currentPlayer);

        String opponent = currentPlayer.equals("RED") ? "BLUE" : "RED";
        boolean captureOccurred = false;
        HashSet<Hexagon> processedOpponents = new HashSet<>();


        // For every hex in the simulated currentGroup, check its neighbors.
        for (Hexagon hex : currentGroup) {
            for (int i = 0; i < 6; i++) {
                Hexagon neighbor = hex.neighbor(i);
                Hexagon boardNeighbor = getBoardHex(neighbor);
                // If the neighbor belongs to the opponent and hasn't been processed...
                if (boardNeighbor != null && opponent.equals(boardNeighbor.getOwner()) &&
                        !processedOpponents.contains(boardNeighbor)) {
                    // Get the full connected opponent group.
                    HashSet<Hexagon> opponentGroup = getConnectedGroup(boardNeighbor, opponent);
                    processedOpponents.addAll(opponentGroup);

                    // If the current player's group (including the new hex) is larger,
                    // add the opponents group to the captured set (so we can erase later).
                    if (currentGroup.size() > opponentGroup.size()) {
                        capturedSet.addAll(opponentGroup);
                    }
                }
            }
        }
        // -- End of simulation --
        // Reset the placed hex's owner
        placedHex.setOwner(null);
        return capturedSet;
    }


    /**
     * Flood-fill algorithm to get connected group of hexagons starting a given hexagon that all share the same owner.
     *
     * @param start The starting hexagon.
     * @param owner The owner of the starting hexagon.
     * @return A HashSet of connected hexagons with the given owner.
     */
    public HashSet<Hexagon> getConnectedGroup(Hexagon start, String owner) {
        HashSet<Hexagon> group = new HashSet<>();
        Queue<Hexagon> queue = new LinkedList<>();
        queue.add(start);
        group.add(start);

        while (!queue.isEmpty()) {
            Hexagon current = queue.poll();
            for (int i = 0; i < 6; i++) {
                Hexagon neighbor = current.neighbor(i);
                Hexagon boardNeighbor = getBoardHex(neighbor); // validate neighbor
                if (boardNeighbor != null && owner.equals(boardNeighbor.getOwner()) && !group.contains(boardNeighbor)) {
                    group.add(boardNeighbor);
                    queue.add(boardNeighbor);
                }
            }
        }
        return group;
    }

    /**
     * Helper method to find and return the actual hexagon from the board that matches the given hexagon's coords.
     * Helps with validation/out-of-bounds issues!
     * @param h The hexagon with coordinates to match.
     * @return The matching hexagon from the board, or null if not found.
     */
    private Hexagon getBoardHex(Hexagon h) {
        for (Hexagon hex : board.getHexagons()) {
            if (hex.q == h.q && hex.r == h.r && hex.s == h.s) {
                return hex;
            }
        }
        return null;
    }

    /**
     * Erases all captured hexagons from the board.
     * @param captured hashset of capture hexagons to erase.
     * @return True if hexagons are erased, false otherwise.
     */
    private boolean eraseHexagons(HashSet<Hexagon> captured) {
        boolean captureOccurred = false;
        for (Hexagon hex : captured) {
            hex.setOwner(null);
            captureOccurred = true;
        }
        return captureOccurred;
    }


    /**
     * Makes a list of all valid moves the currentPLayer can make on the board
     * @return List of the valid hexagons the current player can choose.
     */
    public List<Hexagon> getValidMoves() {
        List<Hexagon> validMoves = new ArrayList<>();
        for (Hexagon hex : board.getHexagons()) {
            // Hex must be unowned to be valid.
            if (hex.getOwner() == null) {
                // A hex is valid if either it is not adjacent to any of the current player's hexagons
                // OR if it creates a capture opportunity when placed.
                if (ownsNeighbor(hex) == !captureMove(hex).isEmpty()) {
                    validMoves.add(hex);
                }
            }
        }
        return validMoves;
    }


    public void switchTurn() {
        // Switch between Players
        currentPlayer = currentPlayer.equals("RED") ? "BLUE" : "RED";

        // Update GUI if it's set
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