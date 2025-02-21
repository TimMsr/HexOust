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

    public static final Hexagon[] DIRECTIONS = {
            new Hexagon(1, 0, -1), new Hexagon(1, -1, 0), new Hexagon(0, -1, 1),
            new Hexagon(-1, 0, 1), new Hexagon(-1, 1, 0), new Hexagon(0, 1, -1)
    };

    public static Hexagon direction(int dir) {
        return DIRECTIONS[dir];
    }

    public Hexagon neighbor(int dir) {
        return add(direction(dir));
    }
}