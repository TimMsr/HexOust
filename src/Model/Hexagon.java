package Model;

/**
 * A hexagon object that represents a single cell on the HexOust board
 * using cube coordinates (q+r+s=0).
 *
 * <p>The class provides basic vector‑like operations
 * (add, subtract, distance)
 * that make Board/Controller code read naturally.</p>
 *
 * <h6>Coordinate system</h6>
 * Each hexagon is identified by three integers <code>(q,r,s)</code> such that
 * the sum is always zero. Moving to one of the six neighbors adds one of the
 * pre‑defined {@link #DIRECTIONS} vectors to the current cube coordinate.</p>
 *
 * <h6>Ownership</h6>
 * The {@code owner} field tracks which player occupies the cell:
 * <ul>
 *   <li>{@code "RED"} – red player</li>
 *   <li>{@code "BLUE"} – blue player</li>
 *   <li>{@code null} – unowned</li>
 * </ul>
 */
public class Hexagon {
    public int q;
    public int r;
    public int s;
    private String owner; // Red, Blue, Null(if unowned)

    public Hexagon(int q, int r, int s) {
        if (q + r + s != 0) {
            throw new IllegalArgumentException("q + r + s must be 0");
        }
        this.q = q;
        this.r = r;
        this.s = s;
        this.owner = null;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    //There are six possible movement directions for a hex in the grid.
    //Every direction is represented as a Hexagon with cube coordinates;
    //(q, r, s) whose sum is zero.
    public static final Hexagon[] DIRECTIONS = {
            new Hexagon(1, 0, -1),  // 0: Right
            new Hexagon(1, -1, 0),  // 1: Top-Right
            new Hexagon(0, -1, 1),  // 2: Top-Left
            new Hexagon(-1, 0, 1),  // 3: Left
            new Hexagon(-1, 1, 0),  // 4: Bottom-Left
            new Hexagon(0, 1, -1)   // 5: Bottom-Right
    };


    /**
     * Returns the movement direction corresponding to the given index.
     * The index must be between 0 and 5, representing the 6 directions
     * E.g. Hexagon newHex = currentHex.add(Hexagon.direction(0)); - Moves right
     **/
    public static Hexagon direction(int dir) {
        return DIRECTIONS[dir];
    }

    public Hexagon neighbor(int dir) {
        return add(direction(dir));
    }

    @Override
    public String toString() {
        return String.format("Hexagon[q:%d, r:%d, s:%d, owner:%s]",
                q, r, s, (owner == null ? "NULL" : owner));
    }
}
