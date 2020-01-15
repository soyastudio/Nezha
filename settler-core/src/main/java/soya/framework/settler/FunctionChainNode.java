package soya.framework.settler;

public class FunctionChainNode implements ProcessNode {
    private final ProcessNodeType type = ProcessNodeType.ARRAY;
    private final ProcessNode[] elements;

    protected FunctionChainNode(ProcessNode[] elements) {
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
