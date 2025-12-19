package project;

public class Boat extends MapElement implements Movable {

    public Boat(int row, int col) {
        super(row, col, 'B');
    }

    @Override
    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

}

