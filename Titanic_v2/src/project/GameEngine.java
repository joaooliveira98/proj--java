package project;

import javax.swing.SwingUtilities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GameEngine {

    private ArrayList<String> levelFiles;
    private int currentLevel = 0;
    private Map currentMap;
    private GUI currentGUI;
    private int vidas = 60; // Vida inicial do jogador
    private String playerName;

    public GameEngine(ArrayList<String> levelFiles, String playerName) {
        this.levelFiles = levelFiles;
        this.playerName = playerName;
        loadLevel(0);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void loadLevel(int levelIndex) {
        if (levelIndex < levelFiles.size()) {
            currentLevel = levelIndex;
            currentMap = new Map(levelFiles.get(levelIndex), this);
        }
    }

    // Método para avançar para o próximo nível, ou mostrar mensagem de fim se for o
    // último
    public void nextLevel() {
        // Move to the next level
        if (currentLevel + 1 < levelFiles.size()) {
            loadLevel(currentLevel + 1);
        } else {
            // Last level completed
            if (currentGUI != null) {
                currentGUI.alert("Fim do Jogo!");
            }
            currentGUI.dispose();
            salvarPontuacao();
            new pontuacao(this);

        }
    }

    private void showWelcomeScreen() {
        SwingUtilities.invokeLater(() -> {
            WelcomeWindow menu = new WelcomeWindow();
            menu.setVisible(true);
        });
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public void loadNextLevel() {
        nextLevel();
    }

    public void setCurrentGUI(GUI gui) {
        this.currentGUI = gui;
        if (this.currentGUI != null) {
            this.currentGUI.updateLives(vidas);
        }
    }

    public int getLives() {
        return vidas;
    }

    public void loseLife() {
        loseLife(1);
    }

    // Método para perder vidas, sobrecarregado para permitir perda de quantidade
    // específica (usado para explosivos)
    public void loseLife(int amount) {
        vidas -= amount;
        if (currentGUI != null) {
            currentGUI.updateLives(vidas);
        }
        if (vidas <= 0) {
            currentGUI.alert("Game Over");

            System.exit(0);
        }
    }

    // Método para ganhar vidas, usado quando o barco toca na sereia
    public void gainLife(int amount) {
        vidas += amount;
        if (currentGUI != null) {
            currentGUI.updateLives(vidas);
        }
    }

    // Método para terminar o jogo quando o barco toca no pirata
    public void gameOver() {
        if (currentGUI != null) {
            currentGUI.alert("Game Over");
        }
        System.exit(0);
    }

    public void salvarPontuacao() {
        try (FileWriter fw = new FileWriter("ranking.txt", true);
                PrintWriter pw = new PrintWriter(fw)) {

            // Salva no formato: Nome;Vidas
            pw.println(this.playerName + ";" + this.vidas);

        } catch (IOException e) {
            System.err.println("Erro ao salvar ranking: " + e.getMessage());
        }
    }
}