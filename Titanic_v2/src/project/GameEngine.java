package project;

import java.util.*;

public class GameEngine {

    private ArrayList<String> levelFiles;
    private int currentLevel = 0;
    private Map currentMap;
    private GUI currentGUI;
    private int vidas = 1000;

    public GameEngine(ArrayList<String> levelFiles) {
        this.levelFiles = levelFiles;
        loadLevel(0);
    }

    public void loadLevel(int levelIndex) {
        if (levelIndex < levelFiles.size()) {
            currentLevel = levelIndex;
            currentMap = new Map(levelFiles.get(levelIndex));
        }
    }

    public void nextLevel() {
        // Move to the next level
        if (currentLevel + 1 < levelFiles.size()) {
            loadLevel(currentLevel + 1);
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
        vidas--;
        if (currentGUI != null) {
            currentGUI.updateLives(vidas);
        }
        if (vidas <= 0) {
            currentGUI.alert("Game Over");
            
            System.exit(0);
        }
    }
}