package Model;

import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Hexagon>> hexagons;
    private ArrayList<ArrayList<Hexagon>> redGroups;
    private ArrayList<ArrayList<Hexagon>> blueGroups;

    public Board() {
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
}