package project;

/**
 * Classe abstrata MapElement - Classe base para todos os elementos do mapa
 * Define as propriedades e metodos comuns a todos os elementos do jogo:
 * - Posicao (linha e coluna)
 * - Simbolo que representa o elemento no mapa
 * 
 * Classes que estendem MapElement: Boat, Rock, Mermaid, Pirate, Vortex, Explosive
 */
public abstract class MapElement {
    // Linha onde o elemento esta posicionado no mapa
    protected int row;
    
    // Coluna onde o elemento esta posicionado no mapa
    protected int col;
    
    // Simbolo que representa o elemento (B=Barco, R=Rocha, M=Sereia, etc.)
    protected char symbol;

    /**
    * Construtor da classe MapElement
    * @param row Linha inicial do elemento
    * @param col Coluna inicial do elemento
    * @param symbol Simbolo que representa o elemento no mapa
     */
    public MapElement(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    /**
    * Obtem a linha atual do elemento
    * @return Linha do elemento
     */
    public int getRow() { return row; }
    
    /**
    * Obtem a coluna atual do elemento
    * @return Coluna do elemento
     */
    public int getCol() { return col; }
    
    /**
     * Obtem o simbolo do elemento
     * @return Simbolo que representa o elemento
     */
    public char getSymbol() { return symbol; }
}
