package  project;
public abstract class MapElement {
    protected int row;
    protected int col;
    protected char symbol;

    public MapElement(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public char getSymbol() { return symbol; }
}
