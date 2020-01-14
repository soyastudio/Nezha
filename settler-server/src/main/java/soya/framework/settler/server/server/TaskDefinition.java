package soya.framework.settler.server.server;

import com.google.gson.JsonElement;
import soya.framework.settler.EvaluateFunction;

public class TaskDefinition {
    private EvaluateFunction[] functions;

    private TaskDefinition(EvaluateFunction[] functions) {
        this.functions = functions;
    }

    public static TaskDefinition parse(JsonElement jsonElement) {
        return new TaskDefinition(EvaluateFunction.toFunctions(jsonElement.getAsString()));
    }
}
