package project;

import java.util.*;

public class GameEngine {

    private ArrayList<String> levelFiles;
    private int currentLevel = 0;
    private Map currentMap;
    private GUI currentGUI;

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
        //TODO: Muda o nivel
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Map getCurrentMap() {
        return currentMap;
    }
}
