package project;

/**
 * Classe Rock - Representa uma rocha (iceberg) no mapa
 * As rochas são obstáculos intransponíveis
 * O barco não pode passar por cima delas
 */
public class Rock extends MapElement {

    /**
     * Construtor do Rock
     * @param row Linha onde a rocha está posicionada
     * @param col Coluna onde a rocha está posicionada
     */
    public Rock(int row, int col) {
        // Chama o construtor da classe pai com símbolo 'R'
        super(row, col, 'R');
    }

}