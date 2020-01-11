package soya.framework.settler;

public class EvaluateArray implements EvaluateTreeNode {
    private final EvaluateTreeNodeType type = EvaluateTreeNodeType.ARRAY;
    private final EvaluateTreeNode[] elements;

    protected EvaluateArray(EvaluateTreeNode[] elements) {
        this.elements = elements;
    }

    @Override
    public EvaluateTreeNodeType getType() {
        return type;
    }

    public EvaluateTreeNode[] getElements() {
        return elements;
    }
}
