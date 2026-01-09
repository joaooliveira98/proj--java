package project;

import java.util.*;

public class GameEngine {

    private ArrayList<String> levelFiles;
    private int currentLevel = 0;
    private Map currentMap;
    private GUI currentGUI;
    private int vidas = 60; // Vida inicial do jogador

    public GameEngine(ArrayList<String> levelFiles) {
        this.levelFiles = levelFiles;
        loadLevel(0);
    }

    public void loadLevel(int levelIndex) {
        if (levelIndex < levelFiles.size()) {
            currentLevel = levelIndex;
            currentMap = new Map(levelFiles.get(levelIndex), this);
        }
    }

    // Método para avançar para o próximo nível, ou mostrar mensagem de fim se for o último
    public void nextLevel() {
        // Move to the next level
        if (currentLevel + 1 < levelFiles.size()) {
            loadLevel(currentLevel + 1);
        } else {
            // Last level completed
            if (currentGUI != null) {
                currentGUI.alert("Fim do Jogo!");
            }
            System.exit(0);
        }
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

    // Método para perder vidas, sobrecarregado para permitir perda de quantidade específica (usado para explosivos)
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
}