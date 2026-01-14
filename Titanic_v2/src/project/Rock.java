package project;

/**
 * Classe Rock - Representa um rochedo (iceberg) no mapa
 * Os rochedos são obstáculos intransponíveis
 * O barco não pode passar por cima deles
 */
public class Rock extends MapElement {

    /**
     * Construtor do Rock
     * @param row Linha onde o rochedo está posicionado
     * @param col Coluna onde o rochedo está posicionado
     */
    public Rock(int row, int col) {
        // Chama o construtor da classe pai com símbolo 'R'
        super(row, col, 'R');
    }

}