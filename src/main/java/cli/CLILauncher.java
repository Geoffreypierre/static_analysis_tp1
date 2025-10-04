package cli;

import java.util.Scanner;
import visitor.CallGraph;

public class CLILauncher extends AbstractLauncher {

    public CLILauncher(String path, boolean isMaven) {
        projectPath = path;
        isMavenProject = isMaven;

        if (isMaven) {
            analyzeMavenProject();
        } else {
            analyzeStandardProject();
        }
        runInteractiveSession();
    }

    private void runInteractiveSession() {
        try (Scanner scanner = new Scanner(System.in)) {
            String command = "";
            while (!command.equals("quitter")) {
                displayMenu();
                System.out.println("Veuillez saisir une commande : ");
                command = scanner.nextLine();
                pauseExecution(1000);

                System.out.println("\n");
                processCommand(command, scanner);
            }
        }
        System.out.println("Fin");
    }

    private void displayMenu() {
        System.out.println("\n");
        System.out.println("Liste des commandes :");
        System.out.println("classesVisitees         : Affiche le nombre de classes visitées");
        System.out.println("packagesVisites         : Affiche le nombre de packages visités");
        System.out.println("compterLignes           : Affiche le nombre total de lignes de code");
        System.out.println("compterMethodes         : Affiche le nombre total de méthodes");
        System.out.println("maxParametre            : Affiche le nombre maximum de paramètres dans une méthode");
        System.out.println("moyenneChamps           : Affiche le nombre moyen de champs par classe");
        System.out.println("moyenneLignesMethodes   : Affiche le nombre moyen de lignes par méthode");
        System.out.println("moyenneMethodesClasse   : Affiche le nombre moyen de méthodes par classe");
        System.out.println("topClassesChamps        : Affiche les 10% de classes ayant le plus de champs");
        System.out.println("topClassesLignes        : Affiche les 10% de classes ayant le plus de lignes");
        System.out.println("topClassesMethodes      : Affiche les 10% de classes ayant le plus de méthodes");
        System.out.println("topClassesDeuxCritere   : Affiche les 10% de classes les plus importantes selon les deux critères");
        System.out.println("classesPlusXMethodes    : Affiche les classes avec plus de X méthodes");
        System.out.println("afficherGrapheCMD       : Affiche le graphe des appels de méthodes dans le terminal");
        System.out.println("afficherGrapheGUI       : Affiche le graphe des appels de méthodes sur UI");
        System.out.println("quitter                 : Quitte le programme");
    }

    private void processCommand(String command, Scanner scanner) {
        switch (command) {
            case "classesVisitees":
                staticAnalyzer.displayNbClass();
                break;

            case "packagesVisites":
                staticAnalyzer.displayNbPackage();
                break;

            case "compterLignes":
                staticAnalyzer.displayNbLine();
                break;

            case "compterMethodes":
                staticAnalyzer.displayNbMethod();
                break;

            case "maxParametre":
                staticAnalyzer.displayNbMaxAttribute();
                break;

            case "moyenneChamps":
                staticAnalyzer.displayAVGAttributeClass();
                break;

            case "moyenneLignesMethodes":
                staticAnalyzer.displayAVGLineMethod();
                break;

            case "moyenneMethodesClasse":
                staticAnalyzer.displayAVGMethodClass();
                break;

            case "topClassesChamps":
                staticAnalyzer.displayTopClassesByAttribute();
                break;

            case "topClassesLignes":
                staticAnalyzer.displayTopClassesByLines();
                break;

            case "topClassesMethodes":
                staticAnalyzer.displayTopClassesByMethod();
                break;

            case "topClassesDeuxCritere":
                staticAnalyzer.displayTopClassesByAttributesAndMethods();
                break;

            case "classesPlusXMethodes":
                handleClassesWithXMethods(scanner);
                break;

            case "afficherGrapheCMD":
                CallGraph.displayCallGraph();
                break;

            case "afficherGrapheGUI":
                CallGraph.visualizeCallGraph();
                break;

            case "quitter":
                break;

            default:
                System.out.println("Commande inexistante !");
                break;
        }
    }

    private void handleClassesWithXMethods(Scanner scanner) {
        System.out.println("Entrez le seuil du nombre de méthodes");
        String input = scanner.nextLine();

        try {
            int threshold = Integer.parseInt(input);
            staticAnalyzer.displayClassesWithMoreThanXMethods(threshold);
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide, Veuillez saisir un entier valide");
        }
    }

    private void pauseExecution(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}