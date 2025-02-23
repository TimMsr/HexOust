package Model;

public class Hexagon {
    public int q;
    public int r;
    public int s;

    public Hexagon(int q, int r, int s) {
        if (q + r + s != 0) {
            throw new IllegalArgumentException("q + r + s must be 0");
        }
        this.q = q;
        this.r = r;
        this.s = s;
    }

    public Hexagon add(Hexagon b) {
        return new Hexagon(q + b.q, r + b.r, s + b.s);
    }

    public Hexagon subtract(Hexagon b) {
        return new Hexagon(q - b.q, r - b.r, s - b.s);
    }

    public int length() {
        return (Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2;
    }

    public int distance(Hexagon b) {
        return subtract(b).length();
    }

    //There are six possible movement diretions in a hex grid.
    //Every direction is repr as a Hexagon with cube coordinates (q, r, s).
    // The sum of q, r, and s has to be 0.
    public static final Hexagon[] DIRECTIONS = {
            new Hexagon(1, 0, -1),  // Right        0
            new Hexagon(1, -1, 0),  // Top-Right    1
            new Hexagon(0, -1, 1),  // Top-Left     2
            new Hexagon(-1, 0, 1),  // Left         3
            new Hexagon(-1, 1, 0),  // Bottom-Left  4
            new Hexagon(0, 1, -1)   // Bottom-Right 5
    };


    // Returns the movement direction corresponding to the given index.
    // The index must be between 0 and 5, representing the 6 directions
    // E.g. Hexagon newHex = currentHex.add(Hexagon.direction(0)); - Moves right
    public static Hexagon direction(int dir) {
        return DIRECTIONS[dir];
    }

    public Hexagon neighbor(int dir) {
        return add(direction(dir));
    }
}