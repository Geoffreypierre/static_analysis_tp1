package visitor;

public class ClassMetrics {

    private final int methodCount;
    private final int fieldCount;
    private final int lineCount;
    private final String simpleName;

    public ClassMetrics(int methodCount, int fieldCount, int lineCount, String simpleName) {
        this.methodCount = methodCount;
        this.fieldCount = fieldCount;
        this.lineCount = lineCount;
        this.simpleName = simpleName;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public int getLineCount() {
        return lineCount;
    }

    public String getSimpleName() {
        return simpleName;
    }
}