package soya.framework.settler.support;

import com.google.gson.JsonElement;
import soya.framework.settler.*;

public class TaskDefinition implements ExecutableNode {

    private String name;
    private FunctionNode[] functions;

    private TaskDefinition(FunctionNode[] functions) {
        if(functions == null || functions.length == 0) {
            throw new IllegalFunctionArgumentException("No function defined.");
        }
        this.functions = functions;
    }

    private TaskDefinition(String name, FunctionNode[] functions) {
        if(functions == null || functions.length == 0) {
            throw new IllegalFunctionArgumentException("No function defined.");
        }
        this.name = name;
        this.functions = functions;
    }

    @Override
    public ProcessNodeType getType() {
        return functions.length == 1 ? ProcessNodeType.FUNCTION : ProcessNodeType.CHAIN;
    }

    public String getName() {
        return name;
    }

    public FunctionNode getFunction() {
        return functions[0];
    }

    public FunctionNode[] getFunctions() {
        return functions;
    }

    public static TaskDefinition create(ExecutableNode node, ProcessSession session) {
        if(node instanceof TaskDefinition) {
            return (TaskDefinition) node;
        } else if(node instanceof FunctionNode) {
            return create((FunctionNode) node);
        } else {
            System.out.println("------------- TODO...");
            return null;
        }
    }

    public static TaskDefinition create(FunctionNode functionNode) {
        return new TaskDefinition(new FunctionNode[] {functionNode});
    }

    public static TaskDefinition create(FunctionNode[] functionNodes) {
        return new TaskDefinition(functionNodes);
    }

    public static TaskDefinition create(String name, FunctionNode functionNode) {
        return new TaskDefinition(name, new FunctionNode[] {functionNode});
    }

    public static TaskDefinition create(String name, FunctionNode[] functionNodes) {
        return new TaskDefinition(name, functionNodes);
    }

    public static TaskDefinition parse(String name, JsonElement jsonElement) {
        TaskDefinition taskDefinition = parse(jsonElement);
        taskDefinition.name = name;
        return taskDefinition;
    }

    public static TaskDefinition parse(JsonElement jsonElement) {
        return new TaskDefinition(FunctionNode.toFunctions(jsonElement.getAsString()));
    }
}
