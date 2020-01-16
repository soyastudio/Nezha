package soya.framework.settler;

public class ChainNode implements ExecutableNode {
    private final ProcessNodeType type = ProcessNodeType.CHAIN;
    private final String name;
    private final FunctionNode[] functions;

    protected ChainNode(String name, FunctionNode[] functions) {
        this.name = name;
        this.functions = functions;
    }

    @Override
    public ProcessNodeType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public FunctionNode[] getFunctions() {
        return functions;
    }
}
