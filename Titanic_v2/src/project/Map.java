package project;

import java.util.*;
import java.io.*;

public class Map {

    private ArrayList<String> grid;
    private Boat boat;

    public Map(String filePath) {
        grid = loadMap(filePath);
        findBoatPosition();
    }

    private ArrayList<String> loadMap(String path) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

   private void findBoatPosition() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                if (grid.get(r).charAt(c) == 'B') {
                    boat = new Boat(r, c);
                    return;
                }
            }
        }
    }

    public ArrayList<String> getGrid() {
        return grid;
    }

    public Boat getBoat() {
        return boat;
    }

   public boolean canMoveTo(int row, int col) {
        if (row < 0 || row >= grid.size() || col < 0 || col >= grid.get(0).length()) {
            return false;
        }
        char cell = grid.get(row).charAt(col);
        return cell != 'X' && cell != 'R';
    }
    public void moveBoat(int newRow, int newCol) {
        if (canMoveTo(newRow, newCol)) {
            boat.move(newRow, newCol);
        }
    }
}
