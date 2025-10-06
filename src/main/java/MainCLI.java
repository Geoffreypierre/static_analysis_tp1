import cli.CLILauncher;
import cli.GUILauncher;

public class MainCLI {

    private static boolean isGUI = false;
    private static boolean isCLI = false;
    private static String projectPath = "";

    public static void main(String[] args) {
        parseArguments(args);
        validateArguments();
        launchApplication();
    }

    private static void parseArguments(String[] args) {
        for (String arg : args) {
             if (arg.equals("-gui")) {
                isGUI = true;
            } else if (arg.equals("-cli")) {
                isCLI = true;
            } else if (arg.startsWith("--path=")) {
                projectPath = arg.substring("--path=".length());
                System.out.println("Chemin du projet défini : " + projectPath);
            } else {
                System.out.println("Argument inconnu : " + arg);
            }
        }
    }

    private static void validateArguments() {
        if (!isCLI && !isGUI) {
            System.out.println("Aucun argument fourni, le mode par défaut '-gui' sera utilisé.");
            isGUI = true;
        } else if (isCLI && isGUI) {
            throw new IllegalArgumentException("Arguments incompatibles : vous ne pouvez pas activer à la fois CLI et GUI.");
        }
    }


    private static void launchApplication() {
        if (isCLI == isGUI) {
            throw new IllegalStateException("Veuillez choisir soit le mode CLI, soit le mode GUI, mais pas les deux.");
        }

        String mode = isCLI ? "CLI" : "GUI";
        System.out.println("Lancement en mode " + mode + "...");

        var launcher = isCLI
                ? new CLILauncher(projectPath)
                : new GUILauncher(projectPath);
    }

}