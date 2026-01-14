package project;

/**
 * Classe Boat - Representa o barco controlado pelo jogador
 * Esta classe estende MapElement e implementa Movable
 * O barco e o elemento principal que o jogador controla no jogo
 */
public class Boat extends MapElement implements Movable {

    /**
    * Construtor do Boat
    * @param row Linha inicial do barco no mapa
    * @param col Coluna inicial do barco no mapa
     */
    public Boat(int row, int col) {
        // Chama o construtor da classe pai com simbolo 'B'
        super(row, col, 'B');
    }

    /**
    * Implementacao do metodo move da interface Movable
    * Move o barco para uma nova posicao no mapa
    * @param newRow Nova linha para mover o barco
    * @param newCol Nova coluna para mover o barco
     */
    @Override
    public void move(int newRow, int newCol) {
        // Atualiza a posição do barco
        this.row = newRow;
        this.col = newCol;
    }

}

