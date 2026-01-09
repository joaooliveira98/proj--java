package project;

import java.util.*;
import java.nio.file.*;

public class Map {

    private ArrayList<String> grid;
    private Boat boat;
    // Lista de Vortexes no mapa para teletransporte
    private ArrayList<Vortex> vortexes;
    // Lista de Sereias no mapa para bónus de vidas
    private ArrayList<Mermaid> mermaids;
    // O Pirata no mapa, que gera explosivos
    private Pirate pirate;
    private GameEngine engine;

    // Construtor que carrega o mapa e inicializa os elementos especiais
    public Map(String filePath, GameEngine engine) {
        this.engine = engine;
        grid = loadMap(filePath);
        vortexes = new ArrayList<>();
        mermaids = new ArrayList<>();
        findBoatPosition();
        findVortexes();
        findMermaids();
        findPirate();
    }

    private ArrayList<String> loadMap(String path) {
        try {
            return new ArrayList<>(Files.readAllLines(Paths.get(path)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
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

    private void findVortexes() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                if (grid.get(r).charAt(c) == 'V') {
                    vortexes.add(new Vortex(r, c));
                }
            }
        }
    }

    // Método para encontrar todas as sereias no mapa
    private void findMermaids() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                if (grid.get(r).charAt(c) == 'M') {
                    mermaids.add(new Mermaid(r, c));
                }
            }
        }
    }

    // Método para encontrar o pirata no mapa
    private void findPirate() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                if (grid.get(r).charAt(c) == 'P') {
                    pirate = new Pirate(r, c);
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
            // Verificar se moveu para um vortex e teletransportar
            if (grid.get(newRow).charAt(newCol) == 'V') {
                teleportBoat();
            }
            // Verificar se moveu para uma sereia e ganhar vidas
            if (grid.get(boat.getRow()).charAt(boat.getCol()) == 'M') {
                engine.gainLife(10);
                removeMermaid(boat.getRow(), boat.getCol());
            }
            // Se o pirata estiver presente, gerar explosivo
            if (pirate != null) {
                spawnExplosive();
            }
            // Verificar colisões após movimentos
            char current = grid.get(boat.getRow()).charAt(boat.getCol());
            if (current == 'P') {
                engine.gameOver();
            } else if (current == 'E') {
                engine.loseLife(10);
                removeExplosive(boat.getRow(), boat.getCol());
            }
        }
    }

    // Método para teletransportar o barco para o outro vortex
    private void teleportBoat() {
        for (Vortex v : vortexes) {
            if (v.getRow() != boat.getRow() || v.getCol() != boat.getCol()) {
                boat.move(v.getRow(), v.getCol());
                break;
            }
        }
    }

    // Método para remover a sereia do mapa após ser tocada
    private void removeMermaid(int row, int col) {
        StringBuilder sb = new StringBuilder(grid.get(row));
        sb.setCharAt(col, '.');
        grid.set(row, sb.toString());
    }

    // Método para obter uma posição vazia aleatória no mapa
    private int[] getRandomEmptyPosition() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(grid.size());
            col = rand.nextInt(grid.get(0).length());
        } while (grid.get(row).charAt(col) != '.');
        return new int[]{row, col};
    }

    // Método para obter uma posição vazia aleatória, excluindo as 4 direções adjacentes ao barco
    private int[] getRandomEmptyPositionExcludingAdjacent() {
        Random rand = new Random();
        int row, col;
        int boatRow = boat.getRow();
        int boatCol = boat.getCol();
        do {
            row = rand.nextInt(grid.size());
            col = rand.nextInt(grid.get(0).length());
        } while (grid.get(row).charAt(col) != '.' ||
                 ((Math.abs(row - boatRow) == 1 && col == boatCol) ||
                  (Math.abs(col - boatCol) == 1 && row == boatRow)));
        return new int[]{row, col};
    }

    // Método para mover o pirata para uma posição aleatória (não usado atualmente)
    private void movePirate() {
        // Remove old pirate position
        int oldRow = pirate.getRow();
        int oldCol = pirate.getCol();
        StringBuilder sb = new StringBuilder(grid.get(oldRow));
        sb.setCharAt(oldCol, '.');
        grid.set(oldRow, sb.toString());
        // Get new position
        int[] newPos = getRandomEmptyPosition();
        pirate = new Pirate(newPos[0], newPos[1]);
        // Place new 'P'
        sb = new StringBuilder(grid.get(newPos[0]));
        sb.setCharAt(newPos[1], 'P');
        grid.set(newPos[0], sb.toString());
    }

    // Método para gerar um explosivo em uma posição aleatória segura
    private void spawnExplosive() {
        int[] pos = getRandomEmptyPositionExcludingAdjacent();
        StringBuilder sb = new StringBuilder(grid.get(pos[0]));
        sb.setCharAt(pos[1], 'E');
        grid.set(pos[0], sb.toString());
    }

    // Método para remover o explosivo do mapa após ser tocado
    private void removeExplosive(int row, int col) {
        StringBuilder sb = new StringBuilder(grid.get(row));
        sb.setCharAt(col, '.');
        grid.set(row, sb.toString());
    }

    public boolean touchedIsland() {
        int row = boat.getRow();
        int col = boat.getCol();
        if (row >= 0 && row < grid.size() && col >= 0 && col < grid.get(row).length()) {
            return grid.get(row).charAt(col) == 'I';
        }
        return false;
    }
}
