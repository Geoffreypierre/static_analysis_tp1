package processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import visitor.ClassMetrics;
import visitor.ClassVisitor;
import visitor.PackageVisitor;

import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;

public class StaticAnalyzer {

    private int nbClass;
    private int packageCount;
    private int nbLine;
    private int nbMethod;
    private double averageLinesPerMethod;
    private double averageMethodsPerClass;
    private double averageFieldsPerClass;
    private int nbMaxAttribute;
    private int topPercentIndex;

    private List<ClassMetrics> allClassMetrics;

    public void analyze(CtModel model) {
        ClassVisitor classVisitor = new ClassVisitor();
        Collection<CtType<?>> allClasses = model.getAllTypes();

        for (CtType<?> type : allClasses) {
            type.accept(classVisitor);
        }

        nbClass = classVisitor.getnbClass();
        nbLine = classVisitor.getnbLine();
        nbMethod = classVisitor.getnbMethod();
        nbMaxAttribute = classVisitor.getnbMaxAttribute();
        allClassMetrics = classVisitor.getClassMetricsList();

        topPercentIndex = Math.max(1, allClassMetrics.size() / 10);

        calculateAverages(classVisitor.getnbLine(), classVisitor.getnbAttribute());

        PackageVisitor packageVisitor = new PackageVisitor();
        Collection<CtPackage> allPackages = model.getAllPackages();

        for (CtPackage pkg : allPackages) {
            pkg.accept(packageVisitor);
        }

        packageCount = packageVisitor.getVisitedPackageCount();
    }

    private void calculateAverages(double totalMethodLines, double totalFields) {
        if (nbMethod != 0) {
            averageLinesPerMethod = totalMethodLines / nbMethod;
        }

        if (nbClass != 0) {
            averageMethodsPerClass = (double) nbMethod / nbClass;
            averageFieldsPerClass = totalFields / nbClass;
        }
    }

    private void sortByMethodCount() {
        allClassMetrics.sort((c1, c2) -> Integer.compare(c2.getMethodCount(), c1.getMethodCount()));
    }

    private void sortByFieldCount() {
        allClassMetrics.sort((c1, c2) -> Integer.compare(c2.getFieldCount(), c1.getFieldCount()));
    }

    private void sortByLineCount() {
        allClassMetrics.sort((c1, c2) -> Integer.compare(c2.getLineCount(), c1.getLineCount()));
    }

    public void displayNbClass() {
        System.out.println("Il y a " + nbClass + " classes");
    }

    public void displayNbPackage() {
        System.out.println("Il y a " + packageCount + " packages");
    }

    public void displayNbLine() {
        System.out.println("L'application totabilise " + nbLine + " lignes de code");
    }

    public void displayNbMethod() {
        System.out.println("Il y a un total de " + nbMethod + " méthodes");
    }

    public void displayNbMaxAttribute() {
        System.out.println("La méthode qui possède le plus d'attributs en possède : " + nbMaxAttribute);
    }

    public void displayAVGLineMethod() {
        System.out.println("Il y a ~ " + averageLinesPerMethod + " lignes par méthodes");
    }

    public void displayAVGMethodClass() {
        System.out.println("Il y a ~ " + averageMethodsPerClass + " méthodes par classe");
    }

    public void displayAVGAttributeClass() {
        System.out.println("Il y a ~ " + averageFieldsPerClass + " attributs par classe");
    }

    public void displayTopClassesByAttribute() {
        System.out.println("Les 10% de classes ayant le plus d'attributs");
        sortByFieldCount();

        for (int i = 0; i < topPercentIndex; i++) {
            ClassMetrics metrics = allClassMetrics.get(i);
            System.out.println("n°" + (i + 1) + " Classe : " + metrics.getSimpleName() + "\n" +
                                                "Nombre d'attributs: " + metrics.getFieldCount());
        }
    }

    public void displayTopClassesByMethod() {
        System.out.println("Les 10% de classes ayant le plus de méthodes");
        sortByMethodCount();

        for (int i = 0; i < topPercentIndex; i++) {
            ClassMetrics metrics = allClassMetrics.get(i);
            System.out.println("n°" + (i + 1) + " Classe : " + metrics.getSimpleName() + "\n" +
                                                "Méthodes: " + metrics.getMethodCount());
        }
    }

    public void displayTopClassesByLines() {
        System.out.println("Les 10% de classes ayant le plus de lignes de codes");
        sortByLineCount();

        for (int i = 0; i < topPercentIndex; i++) {
            ClassMetrics metrics = allClassMetrics.get(i);
            System.out.println("n°" + (i + 1) + " Classe : " + metrics.getSimpleName() + "\n" +
                                                "Nombre de ligne : " + metrics.getLineCount());
        }
    }

    public void displayTopClassesByAttributesAndMethods() {
        System.out.println("Les 10% de classes ayant le plus de méthodes et de lignes de codes");

        sortByFieldCount();
        List<ClassMetrics> topByFields = new ArrayList<>(allClassMetrics.subList(0, topPercentIndex));

        sortByMethodCount();
        List<ClassMetrics> topByMethods = new ArrayList<>(allClassMetrics.subList(0, topPercentIndex));

        List<ClassMetrics> intersection = new ArrayList<>();
        for (ClassMetrics metrics : topByFields) {
            if (topByMethods.contains(metrics)) {
                intersection.add(metrics);
            }
        }

        for (int i = 0; i < intersection.size(); i++) {
            ClassMetrics metrics = intersection.get(i);
            System.out.println("n°" + (i + 1) + " Classe : " + metrics.getSimpleName() + "\n" +
                    "Nombre d'attribut : " + metrics.getFieldCount() + "\n" +
                    "Nombre de méthodes : " + metrics.getMethodCount());
        }
    }

    public void displayClassesWithMoreThanXMethods(int min) {
        System.out.println("Classe possèdant au moins " + min + " méthodes :");
        for (ClassMetrics metrics : allClassMetrics) {
            if (metrics.getMethodCount() > min) {
                System.out.println("Classe : " + metrics.getSimpleName() + "\n" +
                                   "Nombre de méthodes : " + metrics.getMethodCount() + "\n");
            }
        }
    }
}