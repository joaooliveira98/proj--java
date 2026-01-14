package project;
import java.util.*;
import java.nio.file.*;

/**
 * Classe Map - Representa o mapa/nivel do jogo
 * Responsavel por:
 * - Carregar o mapa a partir de um ficheiro de texto
 * - Gerir todos os elementos do mapa
 * - Controlar movimentos e interacoes do barco com elementos
 * - Gerir mecanicas especiais (teletransporte, ganhar/perder vidas, game over)
 */
public class Map {

    // Grelha do mapa (cada String representa uma linha)
    private ArrayList<String> grid;
    
    // Barco controlado pelo jogador
    private Boat boat;
    
    // Lista de portais no mapa
    private ArrayList<Vortex> vortexes;
    
    // Lista de sereias no mapa
    private ArrayList<Mermaid> mermaids;
    
    // Pirata no mapa
    private Pirate pirate;
    
    // Referencia ao motor do jogo
    private GameEngine engine;

    /**
    * Construtor do Map
    * Carrega o mapa e inicializa todos os elementos especiais
    * @param filePath Caminho do ficheiro de texto com o mapa
    * @param engine Motor do jogo para controlar mecanicas
     */
    public Map(String filePath, GameEngine engine) {
        this.engine = engine;
        // Carrega a grelha do mapa do ficheiro
        grid = loadMap(filePath);
        // Inicializa as listas de elementos especiais
        vortexes = new ArrayList<>();
        mermaids = new ArrayList<>();
        // Procura e inicializa todos os elementos no mapa
        findBoatPosition();
        findVortexes();
        findMermaids();
        findPirate();
    }

    /**
    * Carrega o mapa a partir de um ficheiro de texto
    * @param path Caminho do ficheiro
    * @return Lista de Strings, cada uma representando uma linha do mapa
     */
    private ArrayList<String> loadMap(String path) {
        try {
            // Le todas as linhas do ficheiro
            return new ArrayList<>(Files.readAllLines(Paths.get(path)));
        } catch (Exception e) {
            // Se houver erro, imprime e retorna lista vazia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
    * Procura a posicao inicial do barco no mapa
     */
    private void findBoatPosition() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                // Se encontrar 'B', cria o barco nessa posicao
                if (grid.get(r).charAt(c) == 'B') {
                    boat = new Boat(r, c);
                    return;
                }
            }
        }
    }

    /**
    * Procura todos os vortices no mapa (simbolo 'V')
    * e adiciona-os a lista
     */
    private void findVortexes() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                if (grid.get(r).charAt(c) == 'V') {
                    vortexes.add(new Vortex(r, c));
                }
            }
        }
    }

    /**
    * Procura todas as sereias no mapa (simbolo 'M')
    * e adiciona-as a lista
     */
    private void findMermaids() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                if (grid.get(r).charAt(c) == 'M') {
                    mermaids.add(new Mermaid(r, c));
                }
            }
        }
    }

    /**
    * Procura o pirata no mapa (simbolo 'P')
     */
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

    /**
    * Obtem a grelha do mapa
     * @return Lista com as linhas do mapa
     */
    public ArrayList<String> getGrid() {
        return grid;
    }

    /**
    * Obtem o barco
     * @return Objeto Boat
     */
    public Boat getBoat() {
        return boat;
    }

    /**
    * Verifica se o barco pode mover-se para uma determinada posicao
    * @param row Linha de destino
    * @param col Coluna de destino
    * @return true se pode mover, false caso contrario
     */
    public boolean canMoveTo(int row, int col) {
        // Verifica se esta dentro dos limites do mapa
        if (row < 0 || row >= grid.size() || col < 0 || col >= grid.get(0).length()) {
            return false;
        }
        // Obtem o simbolo da celula de destino
        char cell = grid.get(row).charAt(col);
        // Nao pode mover para 'X' (fora do mapa) nem 'R' (rocha)
        return cell != 'X' && cell != 'R';
    }

    /**
    * Move o barco para uma nova posicao e verifica interacoes
     * @param newRow Nova linha
     * @param newCol Nova coluna
     */
    public void moveBoat(int newRow, int newCol) {
        // Verifica se o movimento e valido
        if (canMoveTo(newRow, newCol)) {
            // Move o barco
            boat.move(newRow, newCol);
            
            // Verifica se moveu para um vortice e teletransporta
            if (grid.get(newRow).charAt(newCol) == 'V') {
                teleportBoat();
            }
            
            // Verifica se moveu para uma sereia e ganha vidas
            if (grid.get(boat.getRow()).charAt(boat.getCol()) == 'M') {
                engine.gainLife(10); // Ganha 10 vidas
                removeMermaid(boat.getRow(), boat.getCol());
            }
            
            // Se o pirata existe, gera um explosivo aleatorio
            if (pirate != null) {
                spawnExplosive();
            }
            
            // Verifica colisoes apos o movimento
            char current = grid.get(boat.getRow()).charAt(boat.getCol());
            
            // Se tocou no pirata, perde 60 vidas e Game Over imediato
            if (current == 'P') {
                engine.pirateHit();  // Perde 10 vidas e Game Over automatico
            } 
            // Se tocou num explosivo, perde 5 vidas
            else if (current == 'E') {
                engine.loseLife(5);
                removeExplosive(boat.getRow(), boat.getCol());
            }
        }
    }

    /**
    * Teletransporta o barco para o outro vortice
    * Quando o barco entra num vortice, e movido para o outro vortice do mapa
     */
    private void teleportBoat() {
        // Procura o vortice diferente do atual
        for (Vortex v : vortexes) {
            // Se nao for o vortice onde o barco esta
            if (v.getRow() != boat.getRow() || v.getCol() != boat.getCol()) {
                // Move o barco para esse vortice
                boat.move(v.getRow(), v.getCol());
                break; // Sai do ciclo
            }
        }
    }

    /**
    * Remove a sereia do mapa apos ser tocada
     * @param row Linha da sereia
     * @param col Coluna da sereia
     */
    private void removeMermaid(int row, int col) {
        // Substitui 'M' por '.' (água)
        StringBuilder sb = new StringBuilder(grid.get(row));
        sb.setCharAt(col, '.');
        grid.set(row, sb.toString());
    }

    /**
    * Obtem uma posicao vazia aleatoria no mapa,
    * excluindo as 4 posicoes adjacentes ao barco (para nao gerar explosivo ao lado)
    * @return Array com [linha, coluna] da posicao escolhida
     */
    private int[] getRandomEmptyPositionExcludingAdjacent() {
        Random rand = new Random();
        int row, col;
        int boatRow = boat.getRow();
        int boatCol = boat.getCol();
        
        // Continua a tentar ate encontrar uma posicao valida
        do {
            row = rand.nextInt(grid.size());
            col = rand.nextInt(grid.get(0).length());
        } while (grid.get(row).charAt(col) != '.' ||  // Nao pode ser celula ocupada
             ((Math.abs(row - boatRow) == 1 && col == boatCol) ||  // Nao pode ser acima/abaixo
              (Math.abs(col - boatCol) == 1 && row == boatRow)));   // Nao pode ser esquerda/direita
        
        return new int[]{row, col};
    }

    /**
    * Gera um explosivo numa posicao aleatoria segura
    * Chamado a cada movimento do barco quando ha pirata no nivel
     */
    private void spawnExplosive() {
        // Obtem posicao aleatoria valida
        int[] pos = getRandomEmptyPositionExcludingAdjacent();
        // Coloca 'E' (explosivo) nessa posicao
        StringBuilder sb = new StringBuilder(grid.get(pos[0]));
        sb.setCharAt(pos[1], 'E');
        grid.set(pos[0], sb.toString());
    }

    /**
    * Remove o explosivo do mapa apos ser tocado
     * @param row Linha do explosivo
     * @param col Coluna do explosivo
     */
    private void removeExplosive(int row, int col) {
        // Substitui 'E' por '.' (agua vazia)
        StringBuilder sb = new StringBuilder(grid.get(row));
        sb.setCharAt(col, '.');
        grid.set(row, sb.toString());
    }

    /**
    * Verifica se o barco tocou na ilha (objetivo do nivel)
    * @return true se tocou na ilha, false caso contrario
     */
    public boolean touchedIsland() {
        int row = boat.getRow();
        int col = boat.getCol();
        // Verifica se esta dentro dos limites e se a celula e 'I' (ilha)
        if (row >= 0 && row < grid.size() && col >= 0 && col < grid.get(row).length()) {
            return grid.get(row).charAt(col) == 'I';
        }
        return false;
    }
}
