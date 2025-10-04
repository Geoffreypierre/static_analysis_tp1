import cli.CLILauncher;
import cli.GUILauncher;

public class MainCLI {

    private static boolean withGui = false;
    private static boolean withCli = false;
    private static boolean isMaven = false;
    private static String projectPath = "";

    public static void main(String[] args) {
        parseArguments(args);
        validateArguments();
        launchApplication();
    }

    private static void parseArguments(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "-isMaven":
                    isMaven = true;
                    break;

                case "-gui":
                    withGui = true;
                    break;

                case "-cli":
                    withCli = true;
                    break;
                default:
                    if (arg.startsWith("--path=")) {
                        projectPath = arg.substring("--path=".length());
                        System.out.println("Project path: " + projectPath);
                    } else {
                        System.out.println("Inconnue : " + arg);
                    }
                    break;
            }
        }
    }

    private static void validateArguments() {
        if (!withCli && !withGui) {
            System.out.println("Argument invalide, argument par défaut : -gui");
            withGui = true;
        }
        if (withCli && withGui) {
            throw new IllegalArgumentException("Trop d'arguments");
        }
    }

    private static void launchApplication() {
        if (withCli) {
            new CLILauncher(projectPath, isMaven);
        } else if (withGui) {
            new GUILauncher(projectPath, isMaven);
        }
    }
}