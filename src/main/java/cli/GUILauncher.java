package cli;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import visitor.CallGraph;

public class GUILauncher extends AbstractLauncher {

    private JFrame frame;
    private JTextArea resultArea;
    private JPanel buttonPanel;
    private PrintStream originalOut;

    public GUILauncher(String path) {
        projectPath = path;
        originalOut = System.out;

        analyzeProject();

        try {
            SwingUtilities.invokeAndWait(this::createAndShowGUI);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (frame != null && frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Analyseur de Code - Métriques");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 750);
        frame.setLayout(new BorderLayout(0, 0));

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Analyse des Métriques");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(236, 240, 241));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel sectionLabel = new JLabel("Métriques disponibles");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionLabel.setForeground(new Color(52, 73, 94));
        sectionLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        leftPanel.add(sectionLabel, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 0, 8));
        buttonPanel.setBackground(new Color(236, 240, 241));

        addMetricButton("📦 Nombre de packages", () -> staticAnalyzer.displayNbPackage(), new Color(52, 152, 219));
        addMetricButton("📋 Nombre de classes", () -> staticAnalyzer.displayNbClass(), new Color(52, 152, 219));
        addMetricButton("📄 Nombre total de lignes", () -> staticAnalyzer.displayNbLine(), new Color(46, 204, 113));
        addMetricButton("⚙️ Nombre total de méthodes", () -> staticAnalyzer.displayNbMethod(), new Color(46, 204, 113));
        addMetricButton("🔢 Nombre maximum de paramètres", () -> staticAnalyzer.displayNbMaxAttribute(), new Color(155, 89, 182));
        addMetricButton("📊 Moyenne de champs par classe", () -> staticAnalyzer.displayAVGAttributeClass(), new Color(155, 89, 182));
        addMetricButton("📏 Moyenne de lignes par méthode", () -> staticAnalyzer.displayAVGLineMethod(), new Color(155, 89, 182));
        addMetricButton("📈 Moyenne de méthodes par classe", () -> staticAnalyzer.displayAVGMethodClass(), new Color(155, 89, 182));
        addMetricButton("🏆 Top 10% classes par champs", () -> staticAnalyzer.displayTopClassesByAttribute(), new Color(230, 126, 34));
        addMetricButton("🏆 Top 10% classes par lignes", () -> staticAnalyzer.displayTopClassesByLines(), new Color(230, 126, 34));
        addMetricButton("🏆 Top 10% classes par méthodes", () -> staticAnalyzer.displayTopClassesByMethod(), new Color(230, 126, 34));
        addMetricButton("🏅 Top 10% classes (2 critères)", () -> staticAnalyzer.displayTopClassesByAttributesAndMethods(), new Color(230, 126, 34));
        addMetricButton("🔍 Classes avec X méthodes", this::handleClassesWithXMethods, new Color(41, 128, 185));
        addMetricButton("📝 Graphe d'appels (texte)", () -> CallGraph.displayCallGraph(), new Color(22, 160, 133));
        addMetricButton("🔗 Visualiser graphe d'appels", () -> CallGraph.visualizeCallGraph(), new Color(22, 160, 133));
        addMetricButton("❌ Quitter", () -> System.exit(0), new Color(231, 76, 60));

        JScrollPane buttonScrollPane = new JScrollPane(buttonPanel);
        buttonScrollPane.setBorder(null);
        buttonScrollPane.setBackground(new Color(236, 240, 241));
        buttonScrollPane.getViewport().setBackground(new Color(236, 240, 241));
        buttonScrollPane.setPreferredSize(new Dimension(320, 0));
        leftPanel.add(buttonScrollPane, BorderLayout.CENTER);

        frame.add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel resultLabel = new JLabel("Résultats");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultLabel.setForeground(new Color(52, 73, 94));
        resultLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        centerPanel.add(resultLabel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBackground(new Color(250, 250, 250));
        resultArea.setForeground(new Color(44, 62, 80));
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        resultArea.setLineWrap(false);
        resultArea.setText("Sélectionnez une métrique pour afficher les résultats");

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        centerPanel.add(resultScrollPane, BorderLayout.CENTER);

        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addMetricButton(String label, Runnable action, Color bgColor) {
        JButton button = new JButton(label);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(280, 36));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        button.addActionListener(e -> {
            resultArea.setText("");
            redirectSystemOut();
            action.run();
            restoreSystemOut();
        });
        buttonPanel.add(button);
    }

    private void redirectSystemOut() {
        System.setOut(new PrintStream(new OutputStream() {
            private StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                if (b == '\n') {
                    resultArea.append(buffer.toString() + "\n");
                    buffer.setLength(0);
                } else {
                    buffer.append((char) b);
                }
            }
        }));
    }

    private void restoreSystemOut() {
        System.setOut(originalOut);
    }

    private void handleClassesWithXMethods() {
        String input = JOptionPane.showInputDialog(frame, "Entrez le seuil du nombre de méthodes :", "Saisie", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                int threshold = Integer.parseInt(input.trim());
                staticAnalyzer.displayClassesWithMoreThanXMethods(threshold);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Entrée invalide. Veuillez saisir un entier valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}