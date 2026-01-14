package project;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Classe GameEngine - Motor do jogo que controla toda a logica do Titanic
 * Responsavel por:
 * - Carregar e gerir os niveis do jogo
 * - Controlar as vidas do jogador
 * - Gerir transicoes entre niveis
 * - Guardar pontuacoes no ficheiro ranking.txt
 * - Controlar condicoes de vitoria e derrota
 */
public class GameEngine {

    // Lista com os caminhos dos ficheiros de niveis
    private ArrayList<String> levelFiles;
    
    // Indice do nivel atual (comeca em 0)
    private int currentLevel = 0;
    
    // Mapa atual sendo jogado
    private Map currentMap;
    
    // Referencia a interface grafica atual
    private GUI currentGUI;
    
    // Vidas do jogador
    private int lives = 60;
    
    // Nome do jogador
    private String playerName;

    // Flag para evitar multiplos Game Over (duplicados)
    private boolean gameOverTriggered = false;

    /**
    * Construtor do GameEngine
    * @param levelFiles Lista com os caminhos dos ficheiros de niveis
     * @param playerName Nome do jogador
     */
    public GameEngine(ArrayList<String> levelFiles, String playerName) {
        this.levelFiles = levelFiles;
        this.playerName = playerName;
        // Carrega o primeiro nivel (indice 0)
        if (levelFiles != null && !levelFiles.isEmpty()) {
            loadLevel(0);
        }
    }

    /**
      * Obtem o nome do jogador
      * @return Nome do jogador
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
    * Carrega um nivel especifico do jogo
    * @param levelIndex Indice do nivel a carregar (0 = primeiro nivel)
     */
    public void loadLevel(int levelIndex) {
        // Verifica se o indice e valido
        if (levelIndex < levelFiles.size()) {
            currentLevel = levelIndex;
            // Cria um novo mapa a partir do ficheiro do nivel
            currentMap = new Map(levelFiles.get(levelIndex), this);
        }
    }

    /**
    * Avanca para o proximo nivel ou termina o jogo se for o ultimo
     */
    public void nextLevel() {
        // Verifica se ha mais niveis disponiveis
        if (currentLevel + 1 < levelFiles.size()) {
            // Carrega o proximo nivel
            loadLevel(currentLevel + 1);
        } else {
            // Ultimo nivel completado - fim do jogo
            if (currentGUI != null) {
                currentGUI.alert("End of Game!");
            }
            // Fecha a janela do jogo
            currentGUI.dispose();
            // Guarda a pontuacao no ficheiro
            saveScore();
            // Abre a tabela de pontuacoes
            new Scoreboard(this);
        }
    }

    /**
    * Obtem o indice do nivel atual
    * @return Indice do nivel atual (0 = primeiro nivel)
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
    * Obtem o mapa atual
     * @return Mapa atual do jogo
     */
    public Map getCurrentMap() {
        return currentMap;
    }

    /**
    * Carrega o proximo nivel (chama nextLevel())
     */
    public void loadNextLevel() {
        nextLevel();
    }

    /**
    * Define a interface grafica atual e atualiza as vidas
    * @param gui Interface grafica a associar ao motor do jogo
     */
    public void setCurrentGUI(GUI gui) {
        this.currentGUI = gui;
        if (this.currentGUI != null) {
            // Atualiza a exibicao de vidas na interface
            this.currentGUI.updateLives(lives);
        }
    }

    /**
    * Obtem o numero de vidas atuais do jogador
    * @return Numero de vidas restantes
     */
    public int getLives() {
        return lives;
    }

    /**
    * Remove uma vida do jogador
     */
    public void loseLife() {
        loseLife(1);
    }

    /**
    * Remove uma quantidade especifica de vidas do jogador
    * @param amount Quantidade de vidas a perder
     */
    public void loseLife(int amount) {
        if (gameOverTriggered) {
            return; // Ja terminou o jogo
        }
        // Subtrai as vidas
        lives -= amount;
        
        // Atualiza a exibicao na interface grafica
        if (currentGUI != null) {
            currentGUI.updateLives(lives);
        }

        // Verifica se o jogador ficou sem vidas
        if (lives <= 0) {
            gameOver();
        }
    }

    /**
    * Adiciona vidas ao jogador (usado quando toca numa sereia)
    * @param amount Quantidade de vidas a ganhar
     */
    public void gainLife(int amount) {
        // Adiciona as vidas
        lives += amount;
        
        // Atualiza a exibicao na interface grafica
        if (currentGUI != null) {
            currentGUI.updateLives(lives);
        }
    }

    /**
     * Quando o barco toca no pirata: Game Over
     */
    public void pirateHit() {
        if (gameOverTriggered) {
            return;
        }
        // Morreu para o pirata: a pontuacao deve ficar a 0
        lives = 0;
        if (currentGUI != null) {
            currentGUI.updateLives(lives);
        }
        gameOver();
    }

    /**
    * Termina o jogo imediatamente (usado quando o barco toca no pirata)
    * Mostra mensagem de Game Over
     */
    public void gameOver() {
        if (gameOverTriggered) {
            return; // Evita popups duplicados
        }
        gameOverTriggered = true;
        // Guarda a pontuacao no ficheiro primeiro
        saveScore();
        
        if (currentGUI != null) {           
            // Cria um dialogo personalizado com botao "View Scores"
            // Passa currentGUI em vez de null para aparecer no meio da janela do jogo
            Object[] options = {"View Scores"};
            JOptionPane.showOptionDialog(currentGUI,
                "Game Over!",
                "End of Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]);
            currentGUI.dispose();
        }
        
        // Abre a tabela de pontuacoes
        new Scoreboard(this);
    }

    /**
    * Guarda a pontuacao do jogador no ficheiro ranking.txt
    * Formato: Nome;Vidas
     */
    public void saveScore() {
        try (FileWriter fw = new FileWriter("ranking.txt", true);
                PrintWriter pw = new PrintWriter(fw)) {

            // Escreve uma linha no formato: Nome;Vidas
            pw.println(this.playerName + ";" + this.lives);

        } catch (IOException e) {
            // Mostra erro se nao conseguir guardar
            System.err.println("Erro ao salvar ranking: " + e.getMessage());
        }
    }
}