package project;

// Importações necessárias para criar a interface gráfica
import java.awt.*;              // Layouts e componentes gráficos
import java.awt.event.*;        // Eventos de teclado e ações
import java.util.ArrayList;     // Para manipular listas
import javax.swing.*;           // Componentes Swing (JFrame, JPanel, JLabel, etc.)

/**
 * Classe GUI - Interface Gráfica do Utilizador
 * Responsável por:
 * - Desenhar o mapa do jogo visualmente
 * - Capturar inputs do teclado (setas) para mover o barco
 * - Atualizar a exibição das vidas
 * - Mostrar mensagens ao jogador
 * - Gerir transições entre níveis
 */
public class GUI extends JFrame implements KeyListener {

    // Mapa atual sendo exibido
    private Map map;
    
    // Motor do jogo para controlar a lógica
    private GameEngine engine;
    
    // Painel onde o mapa é desenhado
    private JPanel panel;
    
    // Etiqueta que mostra as vidas do jogador
    private JLabel livesLabel;
    
    // Nome do jogador (para exibir no título)
    private String playerName;

    // Icones dos elementos do jogo
    private ImageIcon boatImg, islandImg, rockImg, mermaidImg, pirateImg, vortexImg, explosiveImg;

    /**
     * Construtor da GUI
     * @param map Mapa a ser exibido
     * @param engine Motor do jogo
     * @param playerName Nome do jogador
     */
    public GUI(Map map, GameEngine engine, String playerName) {
        this.map = map;
        this.engine = engine;
        this.playerName = playerName;
        // Associa esta GUI ao motor do jogo
        engine.setCurrentGUI(this);

        // Carrega as imagens dos ícones
        loadIcons();

        // Cria o painel principal com layout de grelha
        panel = new JPanel();
        panel.setLayout(new GridLayout(
            map.getGrid().size(),            // Número de linhas
            map.getGrid().get(0).length()    // Número de colunas
        ));

        // Cria painel superior para mostrar as vidas
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        livesLabel = new JLabel("Vidas: " + engine.getLives());
        topPanel.add(livesLabel);
        add(topPanel, BorderLayout.NORTH);

        // Desenha o mapa pela primeira vez
        drawMap();

        // Atualiza o título da janela com o nível e nome
        updateTitle();
        
        // Define o tamanho da janela
        setSize(600, 600);
        
        // Define que a aplicação fecha ao fechar a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Adiciona o painel à janela
        add(panel);
        
        // Adiciona listener de teclado para capturar as setas
        addKeyListener(this);
        
        // Torna a janela visível
        setVisible(true);
    }

    /**
     * Carrega as imagens dos ícones dos elementos do jogo
     */
    private void loadIcons() {
        boatImg = new ImageIcon("bin/resources/icons/boat.png");
        islandImg = new ImageIcon("bin/resources/icons/island.png");
        rockImg = new ImageIcon("bin/resources/icons/iceberg.png");
        mermaidImg = new ImageIcon("bin/resources/icons/mermaid.png");
        pirateImg = new ImageIcon("bin/resources/icons/pirate.png");
        vortexImg = new ImageIcon("bin/resources/icons/vortex.png");
        explosiveImg = new ImageIcon("bin/resources/icons/explosive.png");
    }

    /**
     * Atualiza o título da janela com o nível atual e nome do jogador
     */
    private void updateTitle() {
        setTitle("Titanic - Level " + (engine.getCurrentLevel() + 1) + " - " + playerName);
    }

    /**
     * Desenha/redesenha o mapa completo na interface
     * Percorre todas as células e coloca o ícone apropriado
     */
    private void drawMap() {
        // Remove todos os componentes antigos
        panel.removeAll();

        // Obtém a grelha e o barco
        ArrayList<String> grid = map.getGrid();
        Boat boat = map.getBoat();

        // Percorre todas as linhas e colunas
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {

                // Cria uma célula (painel) com borda
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Se for a posição do barco, mostra o ícone do barco
                if (boat.getRow() == r && boat.getCol() == c) {
                    cell.add(new JLabel(boatImg), BorderLayout.CENTER);
                    cell.setBackground(new Color(0, 70, 204)); // Azul escuro
                } else {
                    // Caso contrário, verifica o símbolo da célula
                    char elem = grid.get(r).charAt(c);

                    switch (elem) {
                        case 'R': // Rochedo (iceberg)
                            cell.add(new JLabel(rockImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'I': // Ilha (objetivo)
                            cell.add(new JLabel(islandImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'P': // Pirata
                            cell.add(new JLabel(pirateImg)); 
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'M': // Sereia
                             cell.add(new JLabel(mermaidImg)); 
                             cell.setBackground(new Color(0, 102, 204));
                             break;
                        case 'V': // Vórtice (portal)
                            cell.add(new JLabel(vortexImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'E': // Explosivo
                            cell.add(new JLabel(explosiveImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case '.': // Agua vazia
                            cell.setBackground(new Color(0, 102, 204)); 
                            break;
                        case 'X': // Fora do mapa
                            cell.setBackground(Color.GRAY); 
                            break;
                        default:  // Qualquer outro símbolo
                            cell.setBackground(new Color(0, 102, 204));
                    }
                }

                // Adiciona a célula ao painel
                panel.add(cell);
            }
        }

        // Atualiza a exibição
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Atualiza a exibição das vidas na interface
     * @param lives Número de vidas atuais
     */
    public void updateLives(int lives) {
        if (livesLabel != null) {
            livesLabel.setText("Vidas: " + lives);
        }
    }

    /**
     * Mostra uma mensagem ao jogador numa janela de diálogo
     * @param message Mensagem a exibir
     */
    public void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Método privado para perder uma vida
     * Chamado a cada movimento do barco
     */
    private void loseLife() {
        engine.loseLife();
        updateLives(engine.getLives());
    }

    /**
     * Método chamado quando uma tecla é pressionada
     * Captura as setas do teclado e move o barco
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Obtém a posição atual do barco
        Boat boat = map.getBoat();
        int row = boat.getRow();
        int col = boat.getCol();
        
        // Verifica qual tecla foi pressionada
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: // Seta esquerda
                if (map.canMoveTo(row, col - 1)) {
                    map.moveBoat(row, col - 1);
                    loseLife(); // Perde 1 vida por movimento
                }
                break;
            case KeyEvent.VK_RIGHT: // Seta direita
                if (map.canMoveTo(row, col + 1)) {
                    map.moveBoat(row, col + 1);
                    loseLife();
                }
                break;
            case KeyEvent.VK_UP: // Seta para cima
                if (map.canMoveTo(row - 1, col)) {
                    map.moveBoat(row - 1, col);
                    loseLife();
                }
                break;
            case KeyEvent.VK_DOWN: // Seta para baixo
                if (map.canMoveTo(row + 1, col)) {
                    map.moveBoat(row + 1, col);
                    loseLife();
                }
                break;
        }
        
        // Redesenha o mapa após o movimento
        drawMap();
        
        // Verifica se tocou na ilha (objetivo)
        if (map.touchedIsland()) {
            // Carrega o próximo nível
            engine.loadNextLevel();
            map = engine.getCurrentMap();
            // Se houver próximo nível, atualiza a interface
            if (map != null) {
                updateTitle();
                drawMap();
            }
        }
    }

    // Métodos da interface KeyListener (não utilizados, mas obrigatórios)
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
