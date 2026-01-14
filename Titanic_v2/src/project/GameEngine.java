package project;

// Importações necessárias
import javax.swing.SwingUtilities;  // Para executar código na thread da interface gráfica
import java.io.FileWriter;          // Para escrever em ficheiros
import java.io.IOException;         // Para tratar exceções de entrada/saída
import java.io.PrintWriter;         // Para escrever texto formatado em ficheiros
import java.util.*;                 // Para usar ArrayList e outras estruturas de dados

/**
 * Classe GameEngine - Motor do jogo que controla toda a lógica do Titanic
 * Responsável por:
 * - Carregar e gerir os níveis do jogo
 * - Controlar as vidas do jogador
 * - Gerir transições entre níveis
 * - Guardar pontuações no ficheiro ranking.txt
 * - Controlar condições de vitória e derrota
 */
public class GameEngine {

    // Lista com os caminhos dos ficheiros de níveis
    private ArrayList<String> levelFiles;
    
    // Indice do nível atual (começa em 0)
    private int currentLevel = 0;
    
    // Mapa atual sendo jogado
    private Map currentMap;
    
    // Referência à interface gráfica atual
    private GUI currentGUI;
    
    // Vidas do jogador (começam em 60)
    private int vidas = 60;
    
    // Nome do jogador
    private String playerName;

    /**
     * Construtor do GameEngine
     * @param levelFiles Lista com os caminhos dos ficheiros de níveis
     * @param playerName Nome do jogador
     */
    public GameEngine(ArrayList<String> levelFiles, String playerName) {
        this.levelFiles = levelFiles;
        this.playerName = playerName;
        // Carrega o primeiro nível (índice 0)
        loadLevel(0);
    }

    /**
     * Obtém o nome do jogador
     * @return Nome do jogador
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Carrega um nível específico do jogo
     * @param levelIndex Indice do nível a carregar (0 = primeiro nível)
     */
    public void loadLevel(int levelIndex) {
        // Verifica se o índice é válido
        if (levelIndex < levelFiles.size()) {
            currentLevel = levelIndex;
            // Cria um novo mapa a partir do ficheiro do nível
            currentMap = new Map(levelFiles.get(levelIndex), this);
        }
    }

    /**
     * Avança para o próximo nível ou termina o jogo se for o último
     */
    public void nextLevel() {
        // Verifica se há mais níveis disponíveis
        if (currentLevel + 1 < levelFiles.size()) {
            // Carrega o próximo nível
            loadLevel(currentLevel + 1);
        } else {
            // Último nível completado - fim do jogo
            if (currentGUI != null) {
                currentGUI.alert("Fim do Jogo!");
            }
            // Fecha a janela do jogo
            currentGUI.dispose();
            // Guarda a pontuação no ficheiro
            salvarPontuacao();
            // Abre a tabela de pontuações
            new pontuacao(this);
        }
    }

    /**
     * Método para mostrar o ecrã de boas-vindas
     * (Não está a ser utilizado atualmente)
     */
    private void showWelcomeScreen() {
        SwingUtilities.invokeLater(() -> {
            WelcomeWindow menu = new WelcomeWindow();
            menu.setVisible(true);
        });
    }

    /**
     * Obtém o índice do nível atual
     * @return Indice do nível atual (0 = primeiro nível)
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Obtém o mapa atual
     * @return Mapa atual do jogo
     */
    public Map getCurrentMap() {
        return currentMap;
    }

    /**
     * Carrega o próximo nível (chama nextLevel())
     */
    public void loadNextLevel() {
        nextLevel();
    }

    /**
     * Define a interface gráfica atual e atualiza as vidas
     * @param gui Interface gráfica a associar ao motor do jogo
     */
    public void setCurrentGUI(GUI gui) {
        this.currentGUI = gui;
        if (this.currentGUI != null) {
            // Atualiza a exibição de vidas na interface
            this.currentGUI.updateLives(vidas);
        }
    }

    /**
     * Obtém o número de vidas atuais do jogador
     * @return Número de vidas restantes
     */
    public int getLives() {
        return vidas;
    }

    /**
     * Remove uma vida do jogador (atalho para loseLife(1))
     */
    public void loseLife() {
        loseLife(1);
    }

    /**
     * Remove uma quantidade específica de vidas do jogador
     * @param amount Quantidade de vidas a perder
     */
    public void loseLife(int amount) {
        // Subtrai as vidas
        vidas -= amount;
        
        // Atualiza a exibição na interface gráfica
        if (currentGUI != null) {
            currentGUI.updateLives(vidas);
        }
        
        // Verifica se o jogador ficou sem vidas
        if (vidas <= 0) {
            // Mostra mensagem de Game Over
            currentGUI.alert("Game Over");
            // Termina o jogo
            System.exit(0);
        }
    }

    /**
     * Adiciona vidas ao jogador (usado quando toca numa sereia)
     * @param amount Quantidade de vidas a ganhar
     */
    public void gainLife(int amount) {
        // Adiciona as vidas
        vidas += amount;
        
        // Atualiza a exibição na interface gráfica
        if (currentGUI != null) {
            currentGUI.updateLives(vidas);
        }
    }

    /**
     * Termina o jogo imediatamente (usado quando o barco toca no pirata)
     */
    public void gameOver() {
        if (currentGUI != null) {
            currentGUI.alert("Game Over");
        }
        // Termina a aplicação
        System.exit(0);
    }

    /**
     * Guarda a pontuação do jogador no ficheiro ranking.txt
     * Formato: Nome;Vidas
     */
    public void salvarPontuacao() {
        try (FileWriter fw = new FileWriter("ranking.txt", true);
                PrintWriter pw = new PrintWriter(fw)) {

            // Escreve uma linha no formato: Nome;Vidas
            pw.println(this.playerName + ";" + this.vidas);

        } catch (IOException e) {
            // Mostra erro se não conseguir guardar
            System.err.println("Erro ao salvar ranking: " + e.getMessage());
        }
    }
}