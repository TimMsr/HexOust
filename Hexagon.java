public class Hexagon {
    private int q;
    private int r;
    private int s;

    public static void main(String[] args) {
        
    }

    public add(Hexagon a) {
        return new Hexagon(q,r,s);
    }

    public remove(Hexagon a) {
        
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


}
