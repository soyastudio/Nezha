package soya.framework.nezha.support;

import com.google.gson.JsonElement;
import soya.framework.nezha.*;

public class TaskDefinition extends Components implements ExecutableNode {

    private String name;
    private FunctionNode[] functions;

    private TaskDefinition(ExecutableNode executableNode) {

    }

    private TaskDefinition(FunctionNode[] functions) {
        if (functions == null || functions.length == 0) {
            throw new IllegalFunctionArgumentException("No function defined.");
        }
        this.functions = functions;
    }

    private TaskDefinition(String name, FunctionNode[] functions) {
        if (functions == null || functions.length == 0) {
            throw new IllegalFunctionArgumentException("No function defined.");
        }
        this.name = name;
        this.functions = functions;
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

    public Processor build(ProcessContext context) throws ProcessorBuildException {
        if (functions == null || functions.length == 0) {
            throw new ProcessorBuildException("");

        } else if (functions.length == 1) {
            FunctionNode functionNode = getFunction();
            return fromFunctionNode(functionNode, context);

        } else {
            ProcessChain.Builder builder = ProcessChain.builder();
            for (FunctionNode fn : functions) {
                builder.add(fromFunctionNode(fn, context));
            }
            return builder.create();
        }
    }


    private Processor fromFunctionNode(FunctionNode functionNode, ProcessContext context) throws ProcessorBuildException {
        // TODO: redefined functions:
        ProcessorBuilder builder = getProcessBuilder(functionNode.getName());

        return builder.build(functionNode.getArguments(), context);
    }

    public static TaskDefinition parse(String name, JsonElement jsonElement) {
        TaskDefinition taskDefinition = parse(jsonElement);
        taskDefinition.name = name;
        return taskDefinition;
    }

    public static TaskDefinition parse(JsonElement jsonElement) {
        return new TaskDefinition(FunctionNode.toFunctions(jsonElement.getAsString()));
    }

    public static Processor create(ExecutableNode node, ProcessContext context) {
        if (node instanceof TaskDefinition) {
            return ((TaskDefinition) node).build(context);
        } else {
            return new TaskDefinition(node).build(context);
        }
    }

}
