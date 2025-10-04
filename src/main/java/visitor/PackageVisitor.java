package visitor;

import spoon.reflect.declaration.CtPackage;
import spoon.reflect.visitor.CtScanner;

import java.util.HashSet;
import java.util.Set;

public class PackageVisitor extends CtScanner {

    private final Set<String> uniquePackages = new HashSet<>();

    @Override
    public void visitCtPackage(CtPackage ctPackage) {
        String packageName = ctPackage.getQualifiedName();

        // Dans cette applciation je ne compte que les sous-package et non les dossier supérieur //
        if (packageName != null && !packageName.isEmpty()
                && !ctPackage.getTypes().isEmpty()) {
            uniquePackages.add(packageName);
        }

        super.visitCtPackage(ctPackage);
    }

    public int getVisitedPackageCount() {
        return uniquePackages.size();
    }
}