package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Classe WelcomeWindow - Janela de boas-vindas do jogo Titanic
 * Esta classe estende JFrame para criar uma janela inicial onde o jogador:
 * - Insere o seu nome
 * - Inicia o jogo
 * - Visualiza as pontuacoes
 */
public class WelcomeWindow extends JFrame {
    // Campo de texto onde o jogador insere o seu nome
    private JTextField nameField;
    
    // Botao para iniciar o jogo
    private JButton startButton;
    
    // Variavel que armazena o nome do jogador
    private String playerName;

    /**
    * Construtor da janela de boas-vindas
    * Inicializa todos os componentes graficos e configura a interface
     */
    public WelcomeWindow() {
        // Define o titulo da janela
        setTitle("Welcome to Titanic");
        
        // Define o tamanho da janela (largura: 300px, altura: 150px)
        setSize(300, 150);
        
        // Define que a aplicacao deve fechar quando a janela for fechada
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Centraliza a janela no ecra
        setLocationRelativeTo(null);

        // Cria um painel com layout em grelha (4 linhas, 1 coluna)
        JPanel panel = new JPanel(new GridLayout(4, 1));

        // Cria uma etiqueta (label) com texto informativo
        JLabel label = new JLabel("Enter your name:");
        
        // Cria o campo de texto para o jogador inserir o nome
        nameField = new JTextField();
        
        // Cria o botao "Start"
        startButton = new JButton("Start");

        // Adiciona um "ouvinte" ao botao Start para detetar quando e clicado
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtem o texto inserido no campo de nome e remove espacos nas extremidades
                playerName = nameField.getText().trim();
                
                // Verifica se o nome nao esta vazio
                if (!playerName.isEmpty()) {
                    // Fecha a janela de boas-vindas
                    dispose();
                    
                    // Inicia o jogo
                    startGame();
                } else {
                    // Se o nome estiver vazio, mostra uma mensagem de erro
                    JOptionPane.showMessageDialog(WelcomeWindow.this, "Please enter a valid name.");
                }
            }
        });
        
        // Cria o botao "View Scores"
        JButton scoreButton = new JButton("View Scores");
        
        // Adiciona um "ouvinte" ao botao View Scores
        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fecha a janela de boas-vindas
                dispose();
                
                // Abre a janela de pontuacoes sem iniciar o jogo
                new Scoreboard(null);
            }
        });

        // Adiciona os componentes ao painel na ordem vertical
        panel.add(label);           // Adiciona a etiqueta "Enter your name:"
        panel.add(nameField);       // Adiciona o campo de texto
        panel.add(startButton);     // Adiciona o botão Start
        panel.add(scoreButton);     // Adiciona o botão Ver Pontuacoes

        // Adiciona o painel a janela principal
        add(panel);
        
        // Torna a janela visivel
        setVisible(true);
    }

    /**
    * Metodo privado que inicia o jogo
    * Carrega todos os niveis e cria as instancias necessarias para comecar a jogar
     */
    private void startGame() {
        // Cria uma lista para armazenar os caminhos dos ficheiros de niveis
        ArrayList<String> levelFiles = new ArrayList<>();
        
        // Adiciona o caminho de cada nivel a lista
        levelFiles.add("src/resources/levels/level1.txt");
        levelFiles.add("src/resources/levels/level2.txt");
        levelFiles.add("src/resources/levels/level3.txt");
        levelFiles.add("src/resources/levels/level4.txt");
        levelFiles.add("src/resources/levels/level5.txt");

        // Cria uma instancia do GameEngine com os niveis e o nome do jogador
        GameEngine engine = new GameEngine(levelFiles, playerName);
        
        // Obtem o mapa atual do motor do jogo
        Map map = engine.getCurrentMap();
        
        // Cria e mostra a interface grafica do jogo (GUI)
        // Passa o mapa, o motor do jogo e o nome do jogador
        new GUI(map, engine, playerName);
    }

    /**
     * Metodo para obter o nome do jogador
     * @return String com o nome do jogador
     */
    public String getPlayerName() {
        return playerName;
    }
}