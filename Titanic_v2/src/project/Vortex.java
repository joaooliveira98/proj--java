package project;

/**
 * Classe Vortex - Representa um vortice (portal) no mapa
 * Quando o barco entra num vortice, e automaticamente teletransportado
 * para o outro vortice existente no mapa
 * E necessario haver pelo menos 2 vortices no mapa para o teletransporte funcionar
 */
public class Vortex extends MapElement {

    /**
    * Construtor do Vortex
    * @param row Linha onde o vortice esta posicionado
    * @param col Coluna onde o vortice esta posicionado
     */
    public Vortex(int row, int col) {
        // Chama o construtor da classe pai com simbolo 'V'
        super(row, col, 'V');
    }

}