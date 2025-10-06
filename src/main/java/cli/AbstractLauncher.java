package cli;

import processor.StaticAnalyzer;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;

public abstract class AbstractLauncher {

    protected static String projectPath;
    protected static boolean isMavenProject;
    protected static StaticAnalyzer staticAnalyzer = new StaticAnalyzer();

    protected static void analyzeProject() {
        Launcher launcher = new Launcher();
        launcher.addInputResource(projectPath);
        launcher.buildModel();

        CtModel model = launcher.getModel();
        staticAnalyzer.analyze(model);
    }

}