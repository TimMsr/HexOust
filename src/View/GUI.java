package View;

import Model.Board;
import Model.Hexagon;
import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Swing front‑end for the <i>HexOust</i> game.
 *
 * <p>{@code GUI} is responsible for:
 * <ul>
 *   <li>Rendering the hexagonal board and highlighting valid moves.</li>
 *   <li>Displaying turn / win information and error messages via the
 *       inner {@code TextDisplay}.</li>
 *   <li>Forwarding mouse clicks to the {@link Controller} and repainting in
 *       response to game‑state changes.</li>
 * </ul>
 *
 * <h6>Collaboration</h6>
 * A two‑way link is established at start‑up:
 * <ol>
 *   <li>The constructor receives a {@link Controller} reference.</li>
 *   <li>The controller’s {@code setGUI(this)} call lets the game logic push
 *       updates back to the view (eg; turn‑change messages).</li>
 * </ol>
 **/
public class GUI extends JFrame {

    private final Board board;
    private final Controller controller;

    // View & helpers
    private final TextDisplay textDisplay = new TextDisplay();
    private JPanel boardPanel;
    private MouseAdapter boardMouseListener;

    // Board layout
    private static final int HEX_SIZE = 30;
    private static final int WIDTH = 750;
    private static final int HEIGHT = 750;


    public GUI(Controller controller) {
        this.controller = controller;
        this.board = controller.getBoard();

        setTitle("HexOust Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Textual info (at top and bottom of screen)
        add(textDisplay, BorderLayout.NORTH);
        add(textDisplay.getErrorLabel(), BorderLayout.SOUTH);

        boardPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        boardPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(boardPanel, BorderLayout.CENTER);

        // Handle mouse clicks
        boardMouseListener = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent pos) {
                Hexagon clicked = board.getHexagonAt(pos.getX(), pos.getY());
                if (clicked == null) return;

                try {
                    controller.handleMove(clicked);
                } catch (IllegalArgumentException ex) {
                    textDisplay.showError("Invalid Cell Placement -> " + clicked);
                }
                repaint();
            }
        };
        boardPanel.addMouseListener(boardMouseListener);

        updateTurnIndicator();
    }

    // Hooks used by controller:
    public void showPassTurnMessage(String txt) { textDisplay.showError(txt); }

    public void updateTurnIndicator() {
        Player current = Player.valueOf(controller.getCurrentPlayer());
        if (controller.getGameOver()) {
            textDisplay.showWinner(current);
            boardPanel.removeMouseListener(boardMouseListener);
        } else {
            textDisplay.showTurn(current);
        }
    }

    // Board rendering:
    private void drawBoard(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = WIDTH / 2, cy = HEIGHT / 2;
        List<Hexagon> valid = controller.getValidMoves();

        for (Hexagon hex : board.getHexagons()) {
            Point p = hexToPixel(hex, cx, cy);
            drawHexagon(g2, p.x, p.y, hex, valid);
        }
    }

    private Point hexToPixel(Hexagon h, int cx, int cy) {
        double x = HEX_SIZE * (3.0 / 2 * h.q);
        double y = HEX_SIZE * (Math.sqrt(3) * (h.r + h.q / 2.0));
        return new Point((int) (cx + x), (int) (cy + y));
    }

    private void drawHexagon(Graphics2D g, int x, int y,
                             Hexagon hex, List<Hexagon> validMoves) {

        int[] xp = new int[6];
        int[] yp = new int[6];
        for (int i = 0; i < 6; i++) {
            double a = Math.toRadians(60 * i);
            xp[i] = (int) (x + HEX_SIZE * Math.cos(a));
            yp[i] = (int) (y + HEX_SIZE * Math.sin(a));
        }

        // Hex colors:
        if (hex.getOwner() == null) {
            g.setColor(validMoves.contains(hex) ? Color.LIGHT_GRAY : Color.GRAY);
        } else if ("RED".equals(hex.getOwner())) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }

        g.fillPolygon(xp, yp, 6);
        g.setColor(Color.BLACK);
        g.drawPolygon(xp, yp, 6);
    }

    // Bootstrap
    public void start() { setVisible(true); }

    public static void main(String[] args) {
        Controller ctrl = new Controller();
        GUI gui = new GUI(ctrl);
        ctrl.setGUI(gui);
        gui.start();
    }

    // Helper classes:
    /** Simple circular color swatch used by TextDisplay. */
    public static class CircleIcon implements Icon {
        private final int diameter;
        private final Color color;
        public CircleIcon(int d, Color c) { diameter = d; color = c; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillOval(x, y, diameter, diameter);
        }
        @Override public int getIconWidth()  { return diameter; }
        @Override public int getIconHeight() { return diameter; }
    }

    /** Semantic type for players, each carries its color. */
    private enum Player {
        RED(Color.RED),
        BLUE(Color.BLUE);
        private final Color colour;
        Player(Color c) { this.colour = c; }
        Color getColour() { return colour; }
    }

    /** A self-contained bar that can display textual messages to the user. */
    private static final class TextDisplay extends JPanel {
        private static final Font STATUS_FONT = new Font("Arial", Font.BOLD, 20);
        private static final Font ERROR_FONT = new Font("Arial", Font.BOLD, 16);
        private static final int  CLEAR_MS = 5_000;

        private final JLabel status = buildLabel(STATUS_FONT, Color.BLACK);
        private final JLabel error = buildLabel(ERROR_FONT,  Color.RED);

        TextDisplay() {
            super(new BorderLayout());
            add(status, BorderLayout.CENTER);
        }

        void showTurn(Player player) {
            status.setIcon(new CircleIcon(15, player.getColour()));
            status.setText(" " + player + " to make a move");
            error.setText("");
        }

        void showWinner(Player player) {
            status.setIcon(new CircleIcon(15, player.getColour()));
            status.setText(" " + player + " wins !");
            error.setText("");
        }

        void showError(String msg) {
            error.setText(msg);
            // Timer only active when passing a player turn:
            if (msg.startsWith("No valid moves available.")) {
                Timer t = new Timer(CLEAR_MS, e -> error.setText(""));
                t.setRepeats(false);
                t.start();
            }
        }

        JLabel getErrorLabel() { return error; }

        private static JLabel buildLabel(Font f, Color fg) {
            JLabel l = new JLabel("", SwingConstants.CENTER);
            l.setFont(f);
            l.setForeground(fg);
            return l;
        }
    }
}