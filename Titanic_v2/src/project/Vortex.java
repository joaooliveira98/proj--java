package project;

/**
 * Classe Vortex - Representa um vórtice (portal) no mapa
 * Quando o barco entra num vórtice, é automaticamente teletransportado
 * para o outro vórtice existente no mapa
 * É necessário haver pelo menos 2 vórtices no mapa para o teletransporte funcionar
 */
public class Vortex extends MapElement {

    /**
     * Construtor do Vortex
     * @param row Linha onde o vórtice está posicionado
     * @param col Coluna onde o vórtice está posicionado
     */
    public Vortex(int row, int col) {
        // Chama o construtor da classe pai com símbolo 'V'
        super(row, col, 'V');
    }

}