package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WelcomeWindow extends JFrame {
    private JTextField nameField;
    private JButton startButton;
    private String playerName;

    public WelcomeWindow() {
        setTitle("Bem-vindo ao Titanic");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JLabel label = new JLabel("Insira o seu nome:");
        nameField = new JTextField();
        startButton = new JButton("Start");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) {
                    dispose(); // Close the welcome window
                    startGame();
                } else {
                    JOptionPane.showMessageDialog(WelcomeWindow.this, "Por favor, insira um nome válido.");
                }
            }
        });

        panel.add(label);
        panel.add(nameField);
        panel.add(startButton);

        add(panel);
        setVisible(true);
    }

    private void startGame() {
        // Initialize the game here
        ArrayList<String> levelFiles = new ArrayList<>();
        levelFiles.add("src/resources/levels/level1.txt");
        levelFiles.add("src/resources/levels/level2.txt");
        levelFiles.add("src/resources/levels/level3.txt");
        levelFiles.add("src/resources/levels/level4.txt");
        levelFiles.add("src/resources/levels/level5.txt");

        GameEngine engine = new GameEngine(levelFiles);
        Map map = engine.getCurrentMap();
        new GUI(map, engine);
    }

    public String getPlayerName() {
        return playerName;
    }
}