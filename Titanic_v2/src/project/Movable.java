package project;

/**
 * Interface Movable - Define o comportamento de elementos que podem mover-se no mapa
 * Qualquer elemento que implemente esta interface deve fornecer uma implementacao
 * do metodo move para definir como o elemento se move
 */
public interface Movable {
    /**
     * Metodo para mover o elemento para uma nova posicao
     * @param newRow Nova linha de destino
     * @param newCol Nova coluna de destino
     */
    void move(int newRow, int newCol);
}
