package View;

import Model.Board;
import Model.Hexagon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private Board board;
    private final int HEX_SIZE = 40;
    private final int WIDTH = 1000;
    private final int HEIGHT = 1000;

    public GUI(Board board) {
        this.board = board;
        setTitle("HexOust Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(panel);
    }

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        for (Hexagon hex : board.getHexagons()) {
            Point p = hexToPixel(hex);
            drawHexagon(g2d, centerX + (int) p.x, centerY + (int) p.y);
        }
    }

    private Point hexToPixel(Hexagon hex) {
        double x = HEX_SIZE * (3.0 / 2 * hex.q);
        double y = HEX_SIZE * (Math.sqrt(3) * (hex.r + hex.q / 2.0));
        return new Point((int)x, (int)y);
    }

    private void drawHexagon(Graphics2D g2d, int x, int y) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            xPoints[i] = (int) (x + HEX_SIZE * Math.cos(angle));
            yPoints[i] = (int) (y + HEX_SIZE * Math.sin(angle));
        }

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillPolygon(xPoints, yPoints, 6);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 6);
    }

    public void start() {
        setVisible(true);
    }

    public static void main(String[] args) {
        Board board = new Board();
        new GUI(board).start();
    }
}