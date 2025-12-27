package project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class GUI extends JFrame implements KeyListener {

    private Map map;
    private GameEngine engine;
    private JPanel panel;

    private ImageIcon boatImg, islandImg, rockImg, mermaidImg, pirateImg, vortexImg, explosiveImg;

    public GUI(Map map, GameEngine engine) {
        this.map = map;
        this.engine = engine;

        loadIcons();

        panel = new JPanel();
        panel.setLayout(new GridLayout(
                map.getGrid().size(),
                map.getGrid().get(0).length()
        ));

        drawMap();

        setTitle("Titanic - Nível " + (engine.getCurrentLevel() + 1));
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        addKeyListener(this);
        setVisible(true);
    }

    private void loadIcons() {
        boatImg = new ImageIcon("bin/resources/icons/boat.png");
        islandImg = new ImageIcon("bin/resources/icons/island.png");
        rockImg = new ImageIcon("bin/resources/icons/iceberg.png");
        mermaidImg = new ImageIcon("bin/resources/icons/mermaid.png");
        pirateImg = new ImageIcon("bin/resources/icons/pirate.png");
        vortexImg = new ImageIcon("bin/resources/icons/vortex.png");
        explosiveImg = new ImageIcon("bin/resources/icons/explosive.png");
    }

    private void drawMap() {
        
        panel.removeAll();

        ArrayList<String> grid = map.getGrid();
        Boat boat = map.getBoat();

        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {

                JPanel cell = new JPanel(new BorderLayout());
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Se for o barco
                if (boat.getRow() == r && boat.getCol() == c) {
                    cell.add(new JLabel(boatImg), BorderLayout.CENTER);
                    cell.setBackground(new Color(0, 70, 204));
                } else {
                    char elem = grid.get(r).charAt(c);

                    switch (elem) {
                        case 'R': 
                            cell.add(new JLabel(rockImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'I': 
                            cell.add(new JLabel(islandImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'P': 
                            cell.add(new JLabel(pirateImg)); 
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'M':
                             cell.add(new JLabel(mermaidImg)); 
                             cell.setBackground(new Color(0, 102, 204));
                             break;
                        case 'V': 
                            cell.add(new JLabel(vortexImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'E': 
                            cell.add(new JLabel(explosiveImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case '.': cell.setBackground(new Color(0, 102, 204)); break;
                        case 'X': cell.setBackground(Color.GRAY); break;
                        default:  cell.setBackground(new Color(0, 102, 204));
                    }
                }

                panel.add(cell);
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Boat boat = map.getBoat();
        int row = boat.getRow();
        int col = boat.getCol();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                map.moveBoat(row, col - 1);
                break;
            case KeyEvent.VK_RIGHT:
                map.moveBoat(row, col + 1);
                break;
            case KeyEvent.VK_UP:
                map.moveBoat(row - 1, col);
                break;
            case KeyEvent.VK_DOWN:
                map.moveBoat(row + 1, col);
                break;
        }
        drawMap();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
