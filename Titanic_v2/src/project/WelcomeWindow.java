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
 * - Visualiza as pontuações
 */
public class WelcomeWindow extends JFrame {
    // Campo de texto onde o jogador insere o seu nome
    private JTextField nameField;
    
    // Botão para iniciar o jogo
    private JButton startButton;
    
    // Variável que armazena o nome do jogador
    private String playerName;

    /**
     * Construtor da janela de boas-vindas
     * Inicializa todos os componentes gráficos e configura a interface
     */
    public WelcomeWindow() {
        // Define o título da janela
        setTitle("Bem-vindo ao Titanic");
        
        // Define o tamanho da janela (largura: 300px, altura: 150px)
        setSize(300, 150);
        
        // Define que a aplicação deve fechar quando a janela for fechada
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Centraliza a janela no ecrã
        setLocationRelativeTo(null);

        // Cria um painel com layout em grelha (3 linhas, 1 coluna)
        JPanel panel = new JPanel(new GridLayout(3, 1));

        // Cria uma etiqueta (label) com texto informativo
        JLabel label = new JLabel("Insira o seu nome:");
        
        // Cria o campo de texto para o jogador inserir o nome
        nameField = new JTextField();
        
        // Cria o botão "Start"
        startButton = new JButton("Start");

        // Adiciona um "ouvinte" ao botão Start para detetar quando é clicado
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtém o texto inserido no campo de nome e remove espaços nas extremidades
                playerName = nameField.getText().trim();
                
                // Verifica se o nome não está vazio
                if (!playerName.isEmpty()) {
                    // Fecha a janela de boas-vindas
                    dispose();
                    
                    // Inicia o jogo
                    startGame();
                } else {
                    // Se o nome estiver vazio, mostra uma mensagem de erro
                    JOptionPane.showMessageDialog(WelcomeWindow.this, "Por favor, insira um nome valido.");
                }
            }
        });
        
        // Cria o botão "Ver Pontuacoes"
        JButton scoreButton = new JButton("Ver Pontuacoes");
        
        // Adiciona um "ouvinte" ao botão Ver Pontuacoes
        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fecha a janela de boas-vindas
                dispose();
                
                // Cria uma instância do GameEngine com lista vazia de níveis e nome "anonimo"
                // Esta instância é apenas para aceder à tabela de pontuações, sem iniciar um jogo real
                GameEngine engine = new GameEngine(new ArrayList<>(), "anonimo");
                
                // Abre a janela de pontuações
                new pontuacao(engine);
            }
        });

        // Adiciona os componentes ao painel na ordem vertical
        panel.add(label);           // Adiciona a etiqueta "Insira o seu nome:"
        panel.add(nameField);       // Adiciona o campo de texto
        panel.add(startButton);     // Adiciona o botão Start
        panel.add(scoreButton);     // Adiciona o botão Ver Pontuacoes

        // Adiciona o painel à janela principal
        add(panel);
        
        // Torna a janela visível
        setVisible(true);
    }

    /**
     * Método privado que inicia o jogo
     * Carrega todos os níveis e cria as instâncias necessárias para começar a jogar
     */
    private void startGame() {
        // Cria uma lista para armazenar os caminhos dos ficheiros de níveis
        ArrayList<String> levelFiles = new ArrayList<>();
        
        // Adiciona o caminho de cada nível à lista
        levelFiles.add("src/resources/levels/level1.txt");
        levelFiles.add("src/resources/levels/level2.txt");
        levelFiles.add("src/resources/levels/level3.txt");
        levelFiles.add("src/resources/levels/level4.txt");
        levelFiles.add("src/resources/levels/level5.txt");

        // Cria uma instância do GameEngine com os níveis e o nome do jogador
        GameEngine engine = new GameEngine(levelFiles, playerName);
        
        // Obtém o mapa atual do motor do jogo
        Map map = engine.getCurrentMap();
        
        // Cria e mostra a interface gráfica do jogo (GUI)
        // Passa o mapa, o motor do jogo e o nome do jogador
        new GUI(map, engine, playerName);
    }

    /**
     * Método público para obter o nome do jogador
     * @return String com o nome do jogador
     */
    public String getPlayerName() {
        return playerName;
    }
}