package Controller;

import Model.Board;
import Model.Hexagon;
import View.GUI;

import java.util.*;
/**
 * Central game‑logic coordinator for <i>HexOust</i>.
 * <p>The Controller owns a single {@link Model.Board} instance and
 * tracks whose turn it is. It provides a narrow API used by the GUI.</p>
 *
 * <h6>Collaboration</h6>
 * The controller notifies the UI through a back‑reference to
 * {@link View.GUI} so the view can refresh the display.
 * No Swing components are manipulated directly here,
 * keeping game logic independent of presentation.
 */
public class Controller {
    private final Board board; // final
    private String currentPlayer;
    private boolean gameOver;
    private GUI gui;

    public Controller() {
        this.board = new Board();
        this.currentPlayer = "RED"; // Game starts with RED
        this.gameOver = false;
    }

    /**
     * Handles currentPlayer moves
     * @param hex The hexagon to place on the board
     */
    public void handleMove(Hexagon hex) {
        // Check currentPLayer has valid moves
        if (!hasValidMoves()) {
            switchTurn();
            if (gui != null) { // For avoiding null pointer exception.
                gui.showPassTurnMessage("No valid moves available. Current turn passed to " + currentPlayer);
            }
            return;
        }

        // If hex is already owned
        if (hex.getOwner() != null) {
            throw new IllegalArgumentException("Invalid Cell Placement -> " + hex);
        }
        // Look for hexagons to capture, return true if captured hexagons are erased
        boolean capMove = eraseHexagons((captureMove(hex)));

        // Win - all opponent's hexagons captured
        if (capMove && checkWin(currentPlayer)) {
            hex.setOwner(currentPlayer);
            setGameOver(true);
            if (gui != null) { // For avoiding null pointer exception.
                gui.updateTurnIndicator();
            }
            return;
        }

        if (ownsNeighbor(hex) && !capMove) {
            throw new IllegalArgumentException("Invalid Cell Placement -> " + hex);
        } else {
            // Valid hex placement
            hex.setOwner(currentPlayer);
            if (!capMove) {
                switchTurn();
            }
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

                    if (currentPlayer.equals(targetNeighbor.getOwner())) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    /**
     * Compares nearby grouped hexagons and their owners to check for a capture move.
     * @param placedHex The hexagon that may invoke a capture move
     * @return HashSet of all opponent hexagons to erase (captured). Returns empty set if move is invalid.
     */
    public HashSet<Hexagon> captureMove(Hexagon placedHex) {
        // Simulate the placement
        placedHex.setOwner(currentPlayer);
        HashSet<Hexagon> currentGroup = getConnectedGroup(placedHex, currentPlayer);

        String opponent = currentPlayer.equals("RED") ? "BLUE" : "RED";

        // Early‑exit; size conflict between the two player's groups
        if (hasSizeConflict(placedHex, currentGroup, opponent)) {
            placedHex.setOwner(null); // reset simulation
            return new HashSet<>(); // invalid - no capture
        }
        // Normal capture logic
        HashSet<Hexagon> capturedSet = findCapturedHexagons(currentGroup, opponent);

        placedHex.setOwner(null); // reset simulation
        return capturedSet;
    }

    /**
     * Checks if the placed hex joins an existing group, that every adjacent opponent
     * group the new hex touches is strictly smaller; otherwise the
     * move is invalid.
     * @return true if there is a size conflict, false otherwise
     */
    private boolean hasSizeConflict(Hexagon placedHex,
                                      HashSet<Hexagon> currentGroup, String opponent) {
        if (!ownsNeighbor(placedHex)) return false;
        for (Hexagon hex : currentGroup) {
            for (int dir = 0; dir < 6; dir++) {
                Hexagon boardNeighbor = getBoardHex(hex.neighbor(dir));

                if (boardNeighbor != null && opponent.equals(boardNeighbor.getOwner())) {
                    HashSet<Hexagon> oppGroup = getConnectedGroup(boardNeighbor, opponent);
                    if (oppGroup.size() >= currentGroup.size()) {
                        return true; // invalid move – opponent group is larger
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gathers all opponent groups that will be captured by the current move.
     * Should be called after check for size conflicts.
     */
    private HashSet<Hexagon> findCapturedHexagons(HashSet<Hexagon> currentGroup,
                                                  String opponent) {

        HashSet<Hexagon> captured = new HashSet<>();
        HashSet<Hexagon> processed = new HashSet<>();

        for (Hexagon hex : currentGroup) {
            for (int dir = 0; dir < 6; dir++) {
                Hexagon boardNeighbor = getBoardHex(hex.neighbor(dir));
                if (boardNeighbor != null &&
                    opponent.equals(boardNeighbor.getOwner()) &&
                    !processed.contains(boardNeighbor)) {

                    HashSet<Hexagon> oppGroup = getConnectedGroup(boardNeighbor, opponent);
                    processed.addAll(oppGroup);

                    if (currentGroup.size() > oppGroup.size()) {
                        captured.addAll(oppGroup);
                    }
                }
            }
        }
        return captured;
    }

    /**
     * Flood-fill algorithm gets a connected group of hexagons from a given hexagon
     * that all share the same owner.
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
                Hexagon boardNeighbor = getBoardHex(neighbor);
                if (boardNeighbor != null && owner.equals(boardNeighbor.getOwner())
                        && !group.contains(boardNeighbor)) {
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
     * @return Valid move list.
     */
    public List<Hexagon> getValidMoves() {
        List<Hexagon> validMoves = new ArrayList<>();
        for (Hexagon hex : board.getHexagons()) {
            // Hex cant be owned, and if ownsNeighbor - then must be a capture.
            if (hex.getOwner() == null &&
                    ownsNeighbor(hex) ^ captureMove(hex).isEmpty()) {
                validMoves.add(hex);
            }
        }
        return validMoves;
    }

    public boolean hasValidMoves() {
        return !(getValidMoves().isEmpty());
    }

    /**
     * Checks if given player has won, verifying the opponent doesn't own a hexagon.
     * @param player The player to check for win condition.
     * @return true if the player has won, false otherwise.
     */
    public boolean checkWin(String player) {
        String opponent = player.equals("RED") ? "BLUE" : "RED";
        for (Hexagon hex : board.getHexagons()) {
            if (opponent.equals(hex.getOwner())) {
                return false;
            }
        }
        return true;
    }

    public void switchTurn() {
        currentPlayer = currentPlayer.equals("RED") ? "BLUE" : "RED";
        if (gui != null) {
            gui.updateTurnIndicator();
        }
    }


    public boolean getGameOver() {
        return gameOver;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(String p) {this.currentPlayer = p;}
    public Board getBoard() {
        return board;
    }
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}