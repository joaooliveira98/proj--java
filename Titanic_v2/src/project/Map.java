package project;
import java.util.*;
import java.nio.file.*;

/**
 * Classe Map - Representa o mapa/nível do jogo
 * Responsável por:
 * - Carregar o mapa a partir de um ficheiro de texto
 * - Gerir todos os elementos do mapa
 * - Controlar movimentos e interações do barco com elementos
 * - Gerir mecânicas especiais (teletransporte, ganhar/perder vidas, game over)
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
    
    // Referência ao motor do jogo
    private GameEngine engine;

    /**
     * Construtor do Map
     * Carrega o mapa e inicializa todos os elementos especiais
     * @param filePath Caminho do ficheiro de texto com o mapa
     * @param engine Motor do jogo para controlar mecânicas
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
            // Lê todas as linhas do ficheiro
            return new ArrayList<>(Files.readAllLines(Paths.get(path)));
        } catch (Exception e) {
            // Se houver erro, imprime e retorna lista vazia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Procura a posição inicial do barco no mapa
     */
    private void findBoatPosition() {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                // Se encontrar 'B', cria o barco nessa posição
                if (grid.get(r).charAt(c) == 'B') {
                    boat = new Boat(r, c);
                    return;
                }
            }
        }
    }

    /**
     * Procura todos os vórtices no mapa (símbolo 'V')
     * e adiciona-os à lista
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
     * Procura todas as sereias no mapa (símbolo 'M')
     * e adiciona-as à lista
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
     * Procura o pirata no mapa (símbolo 'P')
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
     * Obtém a grelha do mapa
     * @return Lista com as linhas do mapa
     */
    public ArrayList<String> getGrid() {
        return grid;
    }

    /**
     * Obtém o barco
     * @return Objeto Boat
     */
    public Boat getBoat() {
        return boat;
    }

    /**
     * Verifica se o barco pode mover-se para uma determinada posição
     * @param row Linha de destino
     * @param col Coluna de destino
     * @return true se pode mover, false caso contrário
     */
    public boolean canMoveTo(int row, int col) {
        // Verifica se está dentro dos limites do mapa
        if (row < 0 || row >= grid.size() || col < 0 || col >= grid.get(0).length()) {
            return false;
        }
        // Obtém o símbolo da célula de destino
        char cell = grid.get(row).charAt(col);
        // Não pode mover para 'X' (fora do mapa) nem 'R' (rocha)
        return cell != 'X' && cell != 'R';
    }

    /**
     * Move o barco para uma nova posição e verifica interações
     * @param newRow Nova linha
     * @param newCol Nova coluna
     */
    public void moveBoat(int newRow, int newCol) {
        // Verifica se o movimento é válido
        if (canMoveTo(newRow, newCol)) {
            // Move o barco
            boat.move(newRow, newCol);
            
            // Verifica se moveu para um vórtice e teletransporta
            if (grid.get(newRow).charAt(newCol) == 'V') {
                teleportBoat();
            }
            
            // Verifica se moveu para uma sereia e ganha vidas
            if (grid.get(boat.getRow()).charAt(boat.getCol()) == 'M') {
                engine.gainLife(10); // Ganha 10 vidas
                removeMermaid(boat.getRow(), boat.getCol());
            }
            
            // Se o pirata existe, gera um explosivo aleatório
            if (pirate != null) {
                spawnExplosive();
            }
            
            // Verifica colisões após o movimento
            char current = grid.get(boat.getRow()).charAt(boat.getCol());
            
            // Se tocou no pirata, perde 10 vidas e Game Over imediato
            if (current == 'P') {
                engine.pirateHit();  // Perde 10 vidas e Game Over automático
            } 
            // Se tocou num explosivo, perde 5 vidas
            else if (current == 'E') {
                engine.loseLife(5);
                removeExplosive(boat.getRow(), boat.getCol());
            }
        }
    }

    /**
     * Teletransporta o barco para o outro vórtice
     * Quando o barco entra num vórtice, é movido para o outro vórtice do mapa
     */
    private void teleportBoat() {
        // Procura o vórtice diferente do atual
        for (Vortex v : vortexes) {
            // Se não for o vórtice onde o barco está
            if (v.getRow() != boat.getRow() || v.getCol() != boat.getCol()) {
                // Move o barco para esse vórtice
                boat.move(v.getRow(), v.getCol());
                break; // Sai do ciclo
            }
        }
    }

    /**
     * Remove a sereia do mapa após ser tocada
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
     * Obtém uma posição vazia aleatória no mapa,
     * excluindo as 4 posições adjacentes ao barco (para não gerar explosivo ao lado)
     * @return Array com [linha, coluna] da posição escolhida
     */
    private int[] getRandomEmptyPositionExcludingAdjacent() {
        Random rand = new Random();
        int row, col;
        int boatRow = boat.getRow();
        int boatCol = boat.getCol();
        
        // Continua a tentar até encontrar uma posição válida
        do {
            row = rand.nextInt(grid.size());
            col = rand.nextInt(grid.get(0).length());
        } while (grid.get(row).charAt(col) != '.' ||  // Não pode ser célula ocupada
                 ((Math.abs(row - boatRow) == 1 && col == boatCol) ||  // Não pode ser acima/abaixo
                  (Math.abs(col - boatCol) == 1 && row == boatRow)));   // Não pode ser esquerda/direita
        
        return new int[]{row, col};
    }

    /**
     * Gera um explosivo numa posição aleatória segura
     * Chamado a cada movimento do barco quando há pirata no nível
     */
    private void spawnExplosive() {
        // Obtém posição aleatória válida
        int[] pos = getRandomEmptyPositionExcludingAdjacent();
        // Coloca 'E' (explosivo) nessa posição
        StringBuilder sb = new StringBuilder(grid.get(pos[0]));
        sb.setCharAt(pos[1], 'E');
        grid.set(pos[0], sb.toString());
    }

    /**
     * Remove o explosivo do mapa após ser tocado
     * @param row Linha do explosivo
     * @param col Coluna do explosivo
     */
    private void removeExplosive(int row, int col) {
        // Substitui 'E' por '.' (água vazia)
        StringBuilder sb = new StringBuilder(grid.get(row));
        sb.setCharAt(col, '.');
        grid.set(row, sb.toString());
    }

    /**
     * Verifica se o barco tocou na ilha (objetivo do nível)
     * @return true se tocou na ilha, false caso contrário
     */
    public boolean touchedIsland() {
        int row = boat.getRow();
        int col = boat.getCol();
        // Verifica se está dentro dos limites e se a célula é 'I' (ilha)
        if (row >= 0 && row < grid.size() && col >= 0 && col < grid.get(row).length()) {
            return grid.get(row).charAt(col) == 'I';
        }
        return false;
    }
}
