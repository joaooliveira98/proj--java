package project;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

/**
 * Classe pontuacao - Janela que exibe a tabela de pontuações (ranking)
 * Esta classe:
 * - Lê as pontuações do ficheiro ranking.txt
 * - Filtra para mostrar apenas a melhor pontuação de cada jogador
 * - Ordena as pontuações da maior para a menor
 * - Exibe numa tabela visual
 * - Permite voltar ao menu inicial
 */
public class pontuacao extends JFrame {
    // Referência ao motor do jogo
    private GameEngine engine;

    /**
     * Construtor da janela de pontuações
     * @param engine Motor do jogo (usado para voltar ao menu)
     */
    public pontuacao(GameEngine engine) {
        this.engine = engine;
        
        // Define o título da janela
        setTitle("Tabela de Pontuacoes");
        
        // Define o tamanho da janela
        setSize(400, 300);
        
        // Define que ao fechar esta janela, não fecha a aplicação toda
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        
        // Centraliza a janela no ecrã
        setLocationRelativeTo(null);
        
        // Define o layout da janela
        setLayout(new BorderLayout());

        // Define os nomes das colunas da tabela
        String[] columnNames = { "Nome", "Pontuacao" };
        
        // Cria o modelo da tabela (não editável)
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição das células
            }
        };

        // HashMap para armazenar apenas a melhor pontuação de cada jogador
        // Chave: nome do jogador, Valor: melhor pontuação
        java.util.Map<String, Integer> melhoresPontuacoes = new java.util.HashMap<>();
        
        try {
            // Abre o ficheiro ranking.txt
            File file = new File("ranking.txt");
            
            // Verifica se o ficheiro existe
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                
                // Lê cada linha do ficheiro
                while (scanner.hasNextLine()) {
                    String linha = scanner.nextLine();
                    
                    // Ignora linhas vazias
                    if (linha.trim().isEmpty()) continue;

                    // Divide a linha pelo separador ';' (formato: Nome;Pontos)
                    String[] dados = linha.split(";");
                    
                    // Verifica se a linha tem 2 elementos (nome e pontos)
                    if (dados.length == 2) {
                        String nome = dados[0];
                        int pontos = Integer.parseInt(dados[1]);

                        // REGRA: Só adiciona/atualiza se:
                        // - O nome não existe no mapa OU
                        // - Os pontos atuais são maiores que os pontos anteriores
                        if (!melhoresPontuacoes.containsKey(nome) || pontos > melhoresPontuacoes.get(nome)) {
                            melhoresPontuacoes.put(nome, pontos);
                        }
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            // Se houver erro ao ler o ficheiro, mostra o erro
            e.printStackTrace();
        }

        // Cria uma lista de objetos Score para ordenação
        ArrayList<Score> listaScores = new ArrayList<>();
        
        // Converte o HashMap para a lista de Score
        for (java.util.Map.Entry<String, Integer> entry : melhoresPontuacoes.entrySet()) {
            listaScores.add(new Score(entry.getKey(), entry.getValue()));
        }

        // Ordena a lista do maior para o menor (ordem decrescente)
        Collections.sort(listaScores, (s1, s2) -> Integer.compare(s2.points, s1.points));

        // Adiciona cada pontuação à tabela
        for (Score s : listaScores) {
            model.addRow(new Object[] { s.name, s.points });
        }

        // Cria a tabela com o modelo
        JTable table = new JTable(model);
        
        // Adiciona a tabela a um painel com scroll
        JScrollPane scrollPane = new JScrollPane(table);

        // Cria o botão "Voltar ao Menu"
        JButton backButton = new JButton("Voltar ao Menu");
        
        // Adiciona ação ao botão
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fecha a janela de pontuações
                dispose();
                // Abre a janela de boas-vindas novamente
                new WelcomeWindow().setVisible(true);
            }
        });

        // Adiciona os componentes à janela
        add(new JLabel("TOP PLAYERS", SwingConstants.CENTER), BorderLayout.NORTH); // Título
        add(scrollPane, BorderLayout.CENTER);                                       // Tabela
        add(backButton, BorderLayout.SOUTH);                                        // Botão

        // Torna a janela visível
        setVisible(true);
    }
}