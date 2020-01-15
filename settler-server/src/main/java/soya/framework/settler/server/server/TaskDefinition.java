package soya.framework.settler.server.server;

import com.google.gson.JsonElement;
import soya.framework.settler.FunctionNode;

public class TaskDefinition {
    private FunctionNode[] functions;

    private TaskDefinition(FunctionNode[] functions) {
        this.functions = functions;
    }

    public static TaskDefinition parse(JsonElement jsonElement) {
        return new TaskDefinition(FunctionNode.toFunctions(jsonElement.getAsString()));
    }
}
