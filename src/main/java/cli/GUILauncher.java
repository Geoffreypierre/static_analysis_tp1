package cli;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import visitor.CallGraph;

public class GUILauncher extends AbstractLauncher {

    private JFrame frame;
    private JTextArea resultArea;
    private JPanel buttonPanel;
    private PrintStream originalOut;

    public GUILauncher(String path, boolean isMaven) {
        projectPath = path;
        isMavenProject = isMaven;
        originalOut = System.out;

        if (isMaven) {
            analyzeMavenProject();
        } else {
            analyzeStandardProject();
        }

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
        frame = new JFrame("Analiseur de Code");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout(10, 10));

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        JLabel titleLabel = new JLabel("Analyse des métriques", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(titleLabel, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addMetricButton("Nombre de packages", () -> staticAnalyzer.displayNbPackage());
        addMetricButton("Nombre de classes", () -> staticAnalyzer.displayNbClass());
        addMetricButton("Nombre total de lignes", () -> staticAnalyzer.displayNbLine());
        addMetricButton("Nombre total de méthodes", () -> staticAnalyzer.displayNbMethod());
        addMetricButton("Nombre maximum de paramètres", () -> staticAnalyzer.displayNbMaxAttribute());
        addMetricButton("Moyenne de champs par classe", () -> staticAnalyzer.displayAVGAttributeClass());
        addMetricButton("Moyenne de lignes par méthode", () -> staticAnalyzer.displayAVGLineMethod());
        addMetricButton("Moyenne de méthodes par classe", () -> staticAnalyzer.displayAVGMethodClass());
        addMetricButton("Top 10% classes par champs", () -> staticAnalyzer.displayTopClassesByAttribute());
        addMetricButton("Top 10% classes par lignes", () -> staticAnalyzer.displayTopClassesByLines());
        addMetricButton("Top 10% classes par méthodes", () -> staticAnalyzer.displayTopClassesByMethod());
        addMetricButton("Top 10% classes par deux critères", () -> staticAnalyzer.displayTopClassesByAttributesAndMethods());
        addMetricButton("Classes avec X méthodes", this::handleClassesWithXMethods);
        addMetricButton("Afficher graphe d'appels (texte)", () -> CallGraph.displayCallGraph());
        addMetricButton("Visualiser graphe d'appels (GUI)", () -> CallGraph.visualizeCallGraph());
        addMetricButton("Quitter", () -> System.exit(0));

        JScrollPane buttonScrollPane = new JScrollPane(buttonPanel);
        buttonScrollPane.setPreferredSize(new Dimension(400, 0));
        frame.add(buttonScrollPane, BorderLayout.WEST);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        frame.add(resultScrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        resultArea.setText("Choisissez les métriques que vous souhaitez observer");
    }

    private void addMetricButton(String label, Runnable action) {
        JButton button = new JButton(label);
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
        String input = JOptionPane.showInputDialog(frame, "Entrez le seuil du nombre de méthodes : ", "Input", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                int threshold = Integer.parseInt(input.trim());
                staticAnalyzer.displayClassesWithMoreThanXMethods(threshold);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Entrée invalide, Veuillez saisir un entier valide", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}