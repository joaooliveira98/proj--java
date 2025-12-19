package project;

import java.util.*;

public class Map {

    private ArrayList<String> grid;
    private Boat boat;

    public Map(String filePath) {
        grid = loadMap(filePath);
        findBoatPosition();
    }

    private ArrayList<String> loadMap(String path) {
        ArrayList<String> lines = new ArrayList<>();

        // TODO: ler o ficheiro do mapa e guardar num arraylist

        return lines;
    }

   private void findBoatPosition() {
    // TODO: procura o boat 
    }

    public ArrayList<String> getGrid() {
        return grid;
    }

    public Boat getBoat() {
        return boat;
    }

   public boolean canMoveTo(int row, int col) {

    // TODO: verifica se o boat se pode mexer para uma dada posiçao (nao pode ser wall nem rock e tem de estar nos limites do mapa)
    return true;
    
}
    public void moveBoat(int newRow, int newCol) {
        //TODO: se poder mexer então mexe
    }
}
