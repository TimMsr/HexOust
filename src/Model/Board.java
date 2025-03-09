package Model;

import java.awt.*;
import java.util.ArrayList;


public class Board {
    private ArrayList<Hexagon> hexagons;

    public Board() {
        hexagons = new ArrayList<>();
        generateHexagonalBoard(7); // Base-7 board
    }

    private void generateHexagonalBoard(int size) {
        for (int q = -size + 1; q < size; q++) {
            int r1 = Math.max(-size + 1, -q - size + 1);
            int r2 = Math.min(size - 1, -q + size - 1);

            for (int r = r1; r <= r2; r++) {
                int s = -q - r;
                hexagons.add(new Hexagon(q, r, s));
            }
        }
    }

    public ArrayList<Hexagon> getHexagons() {
        return hexagons;
    }

    /*
     * Finds the hexagon that was clicked on the board, based on pixel coords [x,y]
     * Same size param as GUI.
     */
    public Hexagon getHexagonAt(int x, int y) {
        // *Change params below when we resize board*
        int centerX = 750 / 2;
        int centerY = 750 / 2;
        int hexSize = 30;

        // Loop over all hexagons
        for (Hexagon hex : hexagons) {
            double hexCenterX = centerX + hexSize * (3.0 / 2 * hex.q);
            double hexCenterY = centerY + hexSize * (Math.sqrt(3) * (hex.r + hex.q / 2.0));
            int[] xPoints = new int[6];
            int[] yPoints = new int[6];

            // Loop over hexagon's 6-points
            for (int i = 0; i < 6; i++) {
                double angle = Math.toRadians(60 * i);
                xPoints[i] = (int) (hexCenterX + hexSize * Math.cos(angle));
                yPoints[i] = (int) (hexCenterY + hexSize * Math.sin(angle));
            }
            Polygon poly = new Polygon(xPoints, yPoints, 6);
            // Hexagon found
            if (poly.contains(x, y)) {
                return hex;
            }
        }
        // Hexagon not found
        return null;
    }
}