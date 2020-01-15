package soya.framework.settler;

public final class EvaluateParameter implements ProcessNode {
    private final ProcessNodeType type = ProcessNodeType.ASSIGNMENT;
    private final String value;

    protected EvaluateParameter(String value) {
        this.value = value;
    }

    @Override
    public ProcessNodeType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getStringValue(ProcessContext context) {
        return value;
    }

    public boolean getBoolean(ProcessContext context) {
        return Boolean.parseBoolean(getStringValue(context));
    }

    public int getInteger(ProcessContext context) {
        return Integer.parseInt(getStringValue(context));
    }

    public static int intValue(ProcessNode node, ProcessContext context) {
        return ((EvaluateParameter)node).getInteger(context);
    }
}
