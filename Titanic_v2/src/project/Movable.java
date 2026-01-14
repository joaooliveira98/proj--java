package project;

/**
 * Interface Movable - Define o comportamento de elementos que podem mover-se no mapa
 * Qualquer elemento que implemente esta interface deve fornecer uma implementação
 * do método move para definir como o elemento se move
 */
public interface Movable {
    /**
     * Método para mover o elemento para uma nova posição
     * @param newRow Nova linha de destino
     * @param newCol Nova coluna de destino
     */
    void move(int newRow, int newCol);
}
