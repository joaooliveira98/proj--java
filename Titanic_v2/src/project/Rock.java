package project;

/**
 * Classe Rock - Representa uma rocha (iceberg) no mapa
 * As rochas sao obstaculos intransponiveis
 * O barco nao pode passar por cima delas
 */
public class Rock extends MapElement {

    /**
    * Construtor do Rock
    * @param row Linha onde a rocha esta posicionada
    * @param col Coluna onde a rocha esta posicionada
     */
    public Rock(int row, int col) {
        // Chama o construtor da classe pai com simbolo 'R'
        super(row, col, 'R');
    }

}