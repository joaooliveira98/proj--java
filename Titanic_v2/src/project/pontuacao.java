package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

public class pontuacao extends JFrame {
    private GameEngine engine;

    public pontuacao(GameEngine engine) {
        this.engine = engine;
        setTitle("Tabela de Pontuacoes");
        setSize(400, 300);
        // Mudei para DISPOSE para não fechar o jogo inteiro ao fechar o ranking
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnNames = { "Nome", "Pontuacao" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        
        java.util.Map<String, Integer> melhoresPontuacoes = new java.util.HashMap<>();
        try {
            File file = new File("ranking.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String linha = scanner.nextLine();
                    if (linha.trim().isEmpty()) continue; // Pula linhas vazias

                    String[] dados = linha.split(";");
                    if (dados.length == 2) {
                        String nome = dados[0];
                        int pontos = Integer.parseInt(dados[1]);

                        // REGRA: Só adiciona se o nome não existir ou se os pontos atuais forem maiores
                        if (!melhoresPontuacoes.containsKey(nome) || pontos > melhoresPontuacoes.get(nome)) {
                            melhoresPontuacoes.put(nome, pontos);
                        }
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Score> listaScores = new ArrayList<>();
        for (java.util.Map.Entry<String, Integer> entry : melhoresPontuacoes.entrySet()) {
            listaScores.add(new Score(entry.getKey(), entry.getValue()));
        }

        // 3. ORDENAR (Do maior para o menor)
        Collections.sort(listaScores, (s1, s2) -> Integer.compare(s2.points, s1.points));

        // 4. ADICIONAR AO MODELO DA TABELA
        for (Score s : listaScores) {
            model.addRow(new Object[] { s.name, s.points });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backButton = new JButton("Voltar ao Menu");
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