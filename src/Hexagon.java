package src;
public class Hexagon {
    private int q;
    private int r;
    private int s;

    public static void main(String[] args) {
        
    }

    public Hexagon(int q, int r, int s) {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    public Hexagon add(Hexagon a) {
        return new Hexagon(q,r,s);
    }

    public void remove(Hexagon a) {
        
    }

    public boolean isAdjacent(Hexagon a) {
        if(q == a.q) {
            if(r == a.r+1 || r == a.r-1) {
                return true;
            }
        }
        if(r == a.r) {
            if(q == a.q+1 || q == a.q-1) {
                return true;
            }
        }
        if(s == a.s) {
            if(q == a.q+1 || q == a.q-1) {
                return true;
            }
        }
        return false;
    }


    public Hexagon subtract(Hexagon b)
    {
        return new Hexagon(q - b.q, r - b.r, s - b.s);
    }

    public int length() {
        return (int) ((Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2);
    }

    public int distance(Hexagon b) {
        return subtract(b).length();
    }

    







}
