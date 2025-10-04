package visitor;

import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.CtScanner;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodVisitor extends CtScanner {

    private int nbMaxAttribute = 0;
    private int nbLine = 0;

    @Override
    public <T> void visitCtMethod(CtMethod<T> method) {
        int parameterCount = method.getParameters().size();
        nbMaxAttribute = Math.max(nbMaxAttribute, parameterCount);

        String sourceCode = method.getOriginalSourceFragment().getSourceCode();
        nbLine = sourceCode.split("\n").length;

        List<CtInvocation<?>> invocations = method.getElements(new TypeFilter<>(CtInvocation.class));
        List<String> calledMethods = invocations.stream()
                .map(CtInvocation::getExecutable)
                .map(ref -> formatMethodReference(ref))
                .collect(Collectors.toList());

        String currentMethod = method.getDeclaringType().getQualifiedName() + "." + method.getSimpleName();
        calledMethods.forEach(calledMethod -> CallGraph.addMethodCall(currentMethod, calledMethod));

        super.visitCtMethod(method);
    }

    private String formatMethodReference(CtExecutableReference<?> ref) {
        if (ref.getDeclaringType() != null) {
            return ref.getDeclaringType().getQualifiedName() + "." + ref.getSimpleName();
        }
        return ref.getSimpleName();
    }

    public int getNbMaxAttribute() {
        return nbMaxAttribute;
    }

    public int getNbLine() {
        return nbLine;
    }
}