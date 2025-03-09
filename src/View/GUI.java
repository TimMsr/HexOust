package View;

import Model.Board;
import Model.Hexagon;
import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GUI extends JFrame {
    private Board board;
    private Controller controller;
    private JLabel statusLabel;
    private JLabel errorLabel;

    // Change sizing
    private final int HEX_SIZE = 30;
    private final int WIDTH = 750;
    private final int HEIGHT = 750;

    public GUI(Controller controller) {
        this.controller = controller;
        this.board = controller.getBoard();

        setTitle("HexOust Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures fully closed when exiting
        setLocationRelativeTo(null); // Centers on screen
        setLayout(new BorderLayout());

        // Create a status label for turn indicator
        statusLabel = new JLabel("Current Turn: " + controller.getCurrentPlayer(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(statusLabel, BorderLayout.NORTH); // Place it at the top of the GUI

        // Error label at the bottom for invalid moves.
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);

        // Drawing panel for board
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Ensures that Panel matches window size
        add(panel, BorderLayout.CENTER);

        // Mouse listener to handle clicks on the board.
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent pos) {
                int clickX = pos.getX();
                int clickY = pos.getY();

                Hexagon clickedHex = board.getHexagonAt(clickX, clickY);
                if (clickedHex != null) { // clickedHex is null if not found
                    try {
                        controller.handleMove(clickedHex);
                        // Valid move, clear any previous error.
                        errorLabel.setText("");
                    } catch (IllegalArgumentException ex) {
                        // Invalid move, display error message at bottom.
                        errorLabel.setText("Invalid Cell Placement -> " + clickedHex);
                    }
                    // Reflect any changes to graphical board
                    repaint();
                }
            }
        });
    }

    // Draws the board by converting hex coordinates to pixel positions.
    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        for (Hexagon hex : board.getHexagons()) {
            Point p = hexToPixel(hex, centerX, centerY);
            drawHexagon(g2d, p.x, p.y, hex);
        }
    }

    // Converts a hexagon's cube coordinates to its pixel center.
    private Point hexToPixel(Hexagon hex, int centerX, int centerY) {
        double x = HEX_SIZE * (3.0 / 2 * hex.q);
        double y = HEX_SIZE * (Math.sqrt(3) * (hex.r + hex.q / 2.0));
        return new Point((int)(centerX + x), (int)(centerY + y));
    }

    // Draws a single hexagon. If a stone is present, fills it with the corresponding color.
    private void drawHexagon(Graphics2D g2d, int x, int y, Hexagon hex) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            xPoints[i] = (int) (x + HEX_SIZE * Math.cos(angle));
            yPoints[i] = (int) (y + HEX_SIZE * Math.sin(angle));
        }

        // Set colour of hexagon
        if (hex.getOwner() == null) {
            g2d.setColor(Color.LIGHT_GRAY);
        } else if (hex.getOwner().equals("RED")) {
            g2d.setColor(Color.RED);
        } else if (hex.getOwner().equals("BLUE")) {
            g2d.setColor(Color.BLUE);
        }
        g2d.fillPolygon(xPoints, yPoints, 6);
        // Outline is black
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 6);
    }



    public void updateTurnIndicator() {
        statusLabel.setText("Current Turn: " + controller.getCurrentPlayer());
    }

    public void start() {
        setVisible(true);
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        GUI gui = new GUI(controller);
        controller.setGUI(gui); // Link GUI to the Controller (two-way interaction)
        gui.start();
    }
}