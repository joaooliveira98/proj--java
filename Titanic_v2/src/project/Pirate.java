package project;

/**
 * Classe Pirate - Representa o pirata no mapa
 * - Causa Game Over se o barco tocar nele
 * - Gera explosivos aleatoriamente no mapa a cada movimento do barco
 */
public class Pirate extends MapElement {

    /**
    * Construtor do Pirate
    * @param row Linha onde o pirata esta posicionado
    * @param col Coluna onde o pirata esta posicionado
     */
    public Pirate(int row, int col) {
        // Chama o construtor da classe pai com simbolo 'P'
        super(row, col, 'P');
    }

}