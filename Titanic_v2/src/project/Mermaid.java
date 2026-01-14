package project;

/**
 * Classe Mermaid - Representa a sereia no mapa
 * Quando o barco toca numa sereia, o jogador ganha 10 vidas
 * A sereia desaparece do mapa apos ser tocada
 */
public class Mermaid extends MapElement {

    /**
    * Construtor da Mermaid
    * @param row Linha onde a sereia esta posicionada
    * @param col Coluna onde a sereia esta posicionada
     */
    public Mermaid(int row, int col) {
        // Chama o construtor da classe pai com simbolo 'M'
        super(row, col, 'M');
    }

}