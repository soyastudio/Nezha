package soya.framework.settler;

public class NamedExecutableNode implements ExecutableNode {
    private String name;
    private ExecutableNode executableNode;

    @Override
    public ProcessNodeType getType() {
        return null;
    }
}
