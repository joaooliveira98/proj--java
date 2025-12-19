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
        // carrega o nivel
    }

    public void nextLevel() {
        //TODO: Muda o nivel
    }

    public Map getCurrentMap() {
        return currentMap;
    }
}
