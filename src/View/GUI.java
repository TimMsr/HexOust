package View;

import Model.Board;
import Model.Hexagon;
import Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private Board board;
    private Controller controller;
    private JLabel statusLabel;
    private final int HEX_SIZE = 40;
    private final int WIDTH = 1000;
    private final int HEIGHT = 1000;

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
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH); // Place it at the top of the GUI

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Ensures that Panel matches window size
        add(panel, BorderLayout.CENTER);
    }

    private void drawBoard(Graphics g) {
        // Graphic Quality ensuring below
        Graphics2D g2d = (Graphics2D) g; // Converts Graphics g to Graphics2D (g2d) for smoother drawing.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Turns on antialiasing

        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        // Loop below, loops through every hexagon, converting hex points to pixel points
        for (Hexagon hex : board.getHexagons()) {
            Point p = hexToPixel(hex);
            drawHexagon(g2d, centerX + (int) p.x, centerY + (int) p.y);
        }
    }

    // Method to convert hex points to pixel points on screen
    private Point hexToPixel(Hexagon hex) {
        double x = HEX_SIZE * (3.0 / 2 * hex.q);
        double y = HEX_SIZE * (Math.sqrt(3) * (hex.r + hex.q / 2.0));
        return new Point((int)x, (int)y); // Returns the screen position
    }

    private void drawHexagon(Graphics2D g2d, int x, int y) {
        // 2 Arrays to store the corners of the games (6 corners)
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        // Loop to calculate the corners of the hexagons (loops through the 6 corners), and rotates by 60 degrees
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            xPoints[i] = (int) (x + HEX_SIZE * Math.cos(angle));
            yPoints[i] = (int) (y + HEX_SIZE * Math.sin(angle));
        }

        g2d.setColor(Color.LIGHT_GRAY); // Sets fill color
        g2d.fillPolygon(xPoints, yPoints, 6); // Fills
        g2d.setColor(Color.BLACK); // Sets border color
        g2d.drawPolygon(xPoints, yPoints, 6); // Draws border color
    }

    public void updateTurnIndicator() {
        //Get cur player from Controller and update label
        statusLabel.setText("Current Turn: " + controller.getCurrentPlayer());
    }

    public void start() {
        setVisible(true);
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        GUI gui = new GUI(controller);

        controller.setGUI(gui); // Link the GUI to the Controller
        gui.start();
    }
}