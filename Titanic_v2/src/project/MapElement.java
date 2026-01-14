package project;

/**
 * Classe abstrata MapElement - Classe base para todos os elementos do mapa
 * Define as propriedades e métodos comuns a todos os elementos do jogo:
 * - Posição (linha e coluna)
 * - Símbolo que representa o elemento no mapa
 * 
 * Classes que estendem MapElement: Boat, Rock, Mermaid, Pirate, Vortex, Explosive
 */
public abstract class MapElement {
    // Linha onde o elemento está posicionado no mapa
    protected int row;
    
    // Coluna onde o elemento está posicionado no mapa
    protected int col;
    
    // Símbolo que representa o elemento (B=Barco, R=Rochedo, M=Sereia, etc.)
    protected char symbol;

    /**
     * Construtor da classe MapElement
     * @param row Linha inicial do elemento
     * @param col Coluna inicial do elemento
     * @param symbol Símbolo que representa o elemento no mapa
     */
    public MapElement(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    /**
     * Obtém a linha atual do elemento
     * @return Linha do elemento
     */
    public int getRow() { return row; }
    
    /**
     * Obtém a coluna atual do elemento
     * @return Coluna do elemento
     */
    public int getCol() { return col; }
    
    /**
     * Obtém o símbolo do elemento
     * @return Símbolo que representa o elemento
     */
    public char getSymbol() { return symbol; }
}
