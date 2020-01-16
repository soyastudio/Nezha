package soya.framework.settler;

public class ArrayNode implements ProcessNode {
    private final ProcessNodeType type = ProcessNodeType.ARRAY;
    private final ProcessNode[] elements;

    protected ArrayNode(ProcessNode[] elements) {
        this.elements = elements;
    }

    @Override
    public ProcessNodeType getType() {
        return type;
    }

    public ProcessNode[] getElements() {
        return elements;
    }
}
