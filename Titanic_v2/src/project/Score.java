package project;

/**
 * Classe Score - Representa uma pontuação de um jogador
 * Armazena o nome do jogador e os pontos obtidos (vidas restantes)
 * Utilizada para ordenar e exibir o ranking de jogadores
 */
class Score {
    // Nome do jogador
    String name;
    
    // Pontuação do jogador (vidas restantes no final do jogo)
    int points;

    /**
     * Construtor do Score
     * @param name Nome do jogador
     * @param points Pontuação obtida (vidas restantes)
     */
    public Score(String name, int points) {
        this.name = name;
        this.points = points;
    }
}
