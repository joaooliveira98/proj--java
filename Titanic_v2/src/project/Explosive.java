package project;

/**
 * Classe Explosive - Representa os explosivos no mapa
 * Explosivos são gerados aleatoriamente pelo pirata durante o jogo
 * Quando o barco toca num explosivo, perde 5 vidas
 */
public class Explosive extends MapElement {

    /**
     * Construtor do Explosive
     * @param row Linha onde o explosivo está posicionado
     * @param col Coluna onde o explosivo está posicionado
     */
    public Explosive(int row, int col) {
        // Chama o construtor da classe pai com símbolo 'E'
        super(row, col, 'E');
    }

}