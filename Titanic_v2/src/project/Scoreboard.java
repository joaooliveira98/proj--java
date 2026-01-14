package project;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

/**
 * Janela de pontuacoes que mostra os rankings.
 */
public class Scoreboard extends JFrame {
    // Referencia ao motor do jogo
    private GameEngine engine;

    /**
     * Construtor da janela de pontuacoes
     * @param engine Motor do jogo (usado para voltar ao menu)
     */
    public Scoreboard(GameEngine engine) {
        this.engine = engine;

        // Define o titulo da janela
        setTitle("Scoreboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Define os nomes das colunas da tabela
        String[] columnNames = { "Name", "Score" };

        // Cria o modelo da tabela (nao editavel)
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edicao das celulas
            }
        };

        // HashMap para armazenar apenas a melhor pontuacao de cada jogador
        java.util.Map<String, Integer> bestScores = new HashMap<>();

        try {
            File file = new File("ranking.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        String name = parts[0];
                        int points = Integer.parseInt(parts[1]);
                        if (!bestScores.containsKey(name) || points > bestScores.get(name)) {
                            bestScores.put(name, points);
                        }
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cria uma lista de objetos Score para ordenacao
        ArrayList<Score> scoreList = new ArrayList<>();
        for (java.util.Map.Entry<String, Integer> entry : bestScores.entrySet()) {
            scoreList.add(new Score(entry.getKey(), entry.getValue()));
        }

        // Ordena do maior para o menor
        scoreList.sort((s1, s2) -> Integer.compare(s2.points, s1.points));

        // Adiciona cada pontuacao a tabela
        for (Score s : scoreList) {
            model.addRow(new Object[] { s.name, s.points });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new WelcomeWindow().setVisible(true);
            }
        });

        add(new JLabel("TOP PLAYERS", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
