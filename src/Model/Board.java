package Model;

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
}