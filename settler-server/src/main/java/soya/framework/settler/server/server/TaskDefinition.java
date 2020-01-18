package soya.framework.settler.server.server;

import com.google.gson.JsonElement;
import soya.framework.settler.ExecutableNode;
import soya.framework.settler.FunctionNode;
import soya.framework.settler.ProcessNodeType;

public class TaskDefinition implements ExecutableNode {
    private String name;
    private FunctionNode[] functions;

    private TaskDefinition(FunctionNode[] functions) {
        this.functions = functions;
    }

    @Override
    public ProcessNodeType getType() {
        return ProcessNodeType.CHAIN;
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
