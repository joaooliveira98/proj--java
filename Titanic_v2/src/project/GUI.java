package project;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Classe GUI - Interface Grafica do Utilizador
 * Responsavel por:
 * - Desenhar o mapa do jogo visualmente
 * - Capturar inputs do teclado (setas) para mover o barco
 * - Atualizar a exibicao das vidas
 * - Mostrar mensagens ao jogador
 * - Gerir transicoes entre niveis
 */
public class GUI extends JFrame implements KeyListener {

    // Mapa atual
    private Map map;
    
    // Motor do jogo para controlar a logica
    private GameEngine engine;
    
    // Painel onde o mapa e desenhado
    private JPanel panel;
    
    // Etiqueta que mostra as vidas do jogador
    private JLabel livesLabel;
    
    // Nome do jogador (para exibir no titulo)
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

        // Carrega as imagens dos icones
        loadIcons();

        // Cria o painel principal com layout de grelha
        panel = new JPanel();
        panel.setLayout(new GridLayout(
            map.getGrid().size(),            // Numero de linhas
            map.getGrid().get(0).length()    // Numero de colunas
        ));

        // Cria painel superior para mostrar as vidas
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        livesLabel = new JLabel("Lives: " + engine.getLives());
        topPanel.add(livesLabel);
        add(topPanel, BorderLayout.NORTH);

        // Desenha o mapa pela primeira vez
        drawMap();

        // Atualiza o titulo da janela com o nivel e nome
        updateTitle();
        
        // Define o tamanho da janela
        setSize(600, 600);
        
        // Centraliza a janela no ecra
        setLocationRelativeTo(null);
        
        // Define que a aplicacao fecha ao fechar a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Adiciona o painel à janela
        add(panel);
        
        // Adiciona listener de teclado para capturar as setas
        addKeyListener(this);
        
        // Torna a janela visivel
        setVisible(true);
    }

    /**
    * Carrega as imagens dos icones dos elementos do jogo
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
    * Atualiza o titulo da janela com o nivel atual e nome do jogador
     */
    private void updateTitle() {
        setTitle("Titanic - Level " + (engine.getCurrentLevel() + 1) + " - " + playerName);
    }

    /**
    * Desenha/redesenha o mapa completo na interface
    * Percorre todas as celulas e coloca o icone apropriado
     */
    private void drawMap() {
        // Remove todos os componentes antigos
        panel.removeAll();

        // Obtem a grelha e o barco
        ArrayList<String> grid = map.getGrid();
        Boat boat = map.getBoat();

        // Percorre todas as linhas e colunas
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {

                // Cria uma celula (painel) com borda
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Se for a posicao do barco, mostra o icone do barco
                if (boat.getRow() == r && boat.getCol() == c) {
                    cell.add(new JLabel(boatImg), BorderLayout.CENTER);
                    cell.setBackground(new Color(0, 70, 204)); // Azul escuro
                } else {
                    // Caso contrario, verifica o simbolo da celula
                    char elem = grid.get(r).charAt(c);

                    switch (elem) {
                        case 'R': // Rocha
                            cell.add(new JLabel(rockImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'I': // Ilha
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
                        case 'V': // Vortice
                            cell.add(new JLabel(vortexImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case 'E': // Explosivo
                            cell.add(new JLabel(explosiveImg));
                            cell.setBackground(new Color(0, 102, 204));
                            break;
                        case '.': // Agua
                            cell.setBackground(new Color(0, 102, 204)); 
                            break;
                        case 'X': // Fora do mapa
                            cell.setBackground(Color.GRAY); 
                            break;
                        default:  // Qualquer outro simbolo
                            cell.setBackground(new Color(0, 102, 204));
                    }
                }

                // Adiciona a celula ao painel
                panel.add(cell);
            }
        }

        // Atualiza a exibicao
        panel.revalidate();
        panel.repaint();
    }

    /**
    * Atualiza a exibicao das vidas na interface
    * @param lives Numero de vidas atuais
     */
    public void updateLives(int lives) {
        if (livesLabel != null) {
            livesLabel.setText("Lives: " + lives);
        }
    }

    /**
    * Mostra uma mensagem ao jogador numa janela de dialogo
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
        
        // Verifica se tocou na ilha
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

    // Metodos da interface KeyListener
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
