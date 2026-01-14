package project;

/**
 * Classe Mermaid - Representa a sereia no mapa
 * Quando o barco toca numa sereia, o jogador ganha 10 vidas
 * A sereia desaparece do mapa após ser tocada
 */
public class Mermaid extends MapElement {

    /**
     * Construtor da Mermaid
     * @param row Linha onde a sereia está posicionada
     * @param col Coluna onde a sereia está posicionada
     */
    public Mermaid(int row, int col) {
        // Chama o construtor da classe pai com símbolo 'M'
        super(row, col, 'M');
    }

}