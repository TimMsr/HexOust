package src;
import java.util.ArrayList;

public class board {
    private ArrayList<ArrayList<Hexagon>> hexagons;
    private ArrayList<ArrayList<Hexagon>> redGroups;
    private ArrayList<ArrayList<Hexagon>> blueGroups;

    public board() {
        hexagons = new ArrayList<ArrayList<Hexagon>>();
        redGroups = new ArrayList<ArrayList<Hexagon>>();
        blueGroups = new ArrayList<ArrayList<Hexagon>>();
    }

    public ArrayList<ArrayList<Hexagon>> getRedGroups() {
        return redGroups;
    }

    public ArrayList<ArrayList<Hexagon>> getBlueGroups() {
        return blueGroups;
    }

    
    public static void main(String[] args) {
        

    }
}