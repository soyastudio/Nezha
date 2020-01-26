package soya.framework.settler;

public class FunctionChainNode implements ExecutableNode {

    private final String name;
    private final FunctionNode[] functions;

    protected FunctionChainNode(String name, FunctionNode[] functions) {
        this.name = name;
        this.functions = functions;
    }

    public String getName() {
        return name;
    }

    public FunctionNode[] getFunctions() {
        return functions;
    }

}
