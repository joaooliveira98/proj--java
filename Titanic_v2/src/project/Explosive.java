package project;

/**
 * Classe Explosive - Representa os explosivos no mapa
 * Explosivos sao gerados aleatoriamente pelo pirata durante o jogo
 * Quando o barco toca num explosivo, perde 5 vidas
 */
public class Explosive extends MapElement {

    /**
    * Construtor do Explosive
    * @param row Linha onde o explosivo esta posicionado
    * @param col Coluna onde o explosivo esta posicionado
     */
    public Explosive(int row, int col) {
        // Chama o construtor da classe pai com simbolo 'E'
        super(row, col, 'E');
    }

}