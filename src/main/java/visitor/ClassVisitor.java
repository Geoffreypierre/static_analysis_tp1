package visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.CtScanner;

public class ClassVisitor extends CtScanner {

    private int nbClass = 0;
    private int nbLine = 0;
    private int nbMethod = 0;
    private double nbMethodLine = 0;
    private double nbAttribute = 0;
    private int nbMaxAttribute = 0;

    private final List<ClassMetrics> classMetricsList = new ArrayList<>();

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {

        if (ctClass.isTopLevel()) {
            nbClass++;
        } else {
            super.visitCtClass(ctClass);
            return;
        }

        String sourceCode = ctClass.getOriginalSourceFragment().getSourceCode();
        int classLineCount = sourceCode.split("\n").length;
        nbLine += classLineCount;

        Collection<CtMethod<?>> methods = ctClass.getMethods();
        nbMethod += methods.size();

        MethodVisitor methodVisitor = new MethodVisitor();
        for (CtMethod<?> method : methods) {
            method.accept(methodVisitor);
            nbMethodLine += methodVisitor.getNbLine();
            nbMaxAttribute = Math.max(nbMaxAttribute, methodVisitor.getNbMaxAttribute());
        }

        Collection<CtFieldReference<?>> fields = ctClass.getAllFields();
        nbAttribute += fields.size();

        ClassMetrics metrics = new ClassMetrics(
                methods.size(),
                fields.size(),
                classLineCount,
                ctClass.getSimpleName()
        );
        classMetricsList.add(metrics);

        super.visitCtClass(ctClass);
    }

    public int getnbClass() {
        return nbClass;
    }

    public int getnbLine() {
        return nbLine;
    }

    public int getnbMethod() {
        return nbMethod;
    }

    public double getnbMethodLine() {
        return nbMethodLine;
    }

    public double getnbAttribute() {
        return nbAttribute;
    }

    public int getnbMaxAttribute() {
        return nbMaxAttribute;
    }

    public List<ClassMetrics> getClassMetricsList() {
        return classMetricsList;
    }
}