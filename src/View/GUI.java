package View;

import Model.Board;
import Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private JPanel panel;
    private Board board;
    private Controller controller;
    private JLabel statusLabel;

    public GUI() {
        controller = new Controller();
        board = controller.getBoard();

        frame = new JFrame("HexOust Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };

        statusLabel = new JLabel("Current Player: " + controller.getCurrentPlayer());
        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void drawBoard(Graphics g) {
        int hexSize = 30;
        int cols = 7; // Number of hexagons per row
        int rows = 7; // Number of hexagons per column
        int xOffset = 100; // Start position for hexagons
        int yOffset = 100;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = xOffset + (int) (col * 1.5 * hexSize);
                int y = yOffset + (int) (row * Math.sqrt(3) * hexSize);

                // Stagger odd rows
                if (col % 2 == 1) {
                    y += (int) (Math.sqrt(3) / 2 * hexSize);
                }

                drawHexagon(g, x, y, hexSize);
            }
        }
    }

    private void drawHexagon(Graphics g, int x, int y, int size) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i;
            xPoints[i] = (int) (x + size * Math.cos(angle));
            yPoints[i] = (int) (y + size * Math.sin(angle));
        }
        g.drawPolygon(xPoints, yPoints, 6);
    }

    public static void main(String[] args) {
        new GUI();
    }
}