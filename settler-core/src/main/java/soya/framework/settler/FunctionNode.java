package soya.framework.settler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class FunctionNode implements ExecutableNode {
    private final String name;
    private final ProcessNode[] arguments;

    private FunctionNode(String name, ProcessNode[] arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public ProcessNode[] getArguments() {
        return arguments;
    }

    public static FunctionNode[] toFunctions(String expression) {
        String exp = expression.trim();
        List<FunctionNode> list = new ArrayList();
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (char c : exp.toCharArray()) {
            if (c == '.' && stack.size() == 0) {
                String param = builder.toString().trim();
                if (param.length() == 0) {
                    throw new IllegalFunctionArgumentException("Argument is empty.");
                }

                list.add(toFunction(builder.toString().trim()));
                builder = new StringBuilder();

            } else {
                builder.append(c);
                if (c == '(') {
                    stack.push(c);

                } else if (c == ')') {
                    Character pop = stack.pop();
                    if (pop != '(') {
                        throw new IllegalFunctionArgumentException("Expecting '(' instead of '" + pop + "'.");
                    }
                }

            }
        }

        if (builder != null) {
            list.add(toFunction(builder.toString().trim()));
        }

        return list.toArray(new FunctionNode[list.size()]);
    }

    public static FunctionNode toFunction(String expression) {
        String exp = expression.trim();
        int start = exp.indexOf('(');
        int end = exp.lastIndexOf(')');
        String func = exp.substring(0, start);
        String params = exp.substring(start + 1, end);
        return new FunctionNode(func, toArray(params));
    }

    private static ProcessNode[] toArray(String params) {
        if (params == null || params.trim().length() == 0) {
            return new ProcessNode[0];
        }

        String exp = params.trim();
        List<ProcessNode> list = new ArrayList();
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (char c : exp.toCharArray()) {
            if (c == ',' && stack.size() == 0) {
                String param = builder.toString().trim();
                if (param.length() == 0) {
                    throw new IllegalFunctionArgumentException("Argument is empty.");
                }

                list.add(toNode(builder.toString().trim()));
                builder = new StringBuilder();

            } else {
                builder.append(c);
                if (c == '(' || c == '[' || c == '{') {
                    stack.push(c);

                } else if (c == ')') {
                    Character pop = stack.pop();
                    if (pop != '(') {
                        throw new IllegalFunctionArgumentException("Expecting '(' instead of '" + pop + "'.");
                    }
                } else if (c == ']') {
                    Character pop = stack.pop();
                    if (pop != '[') {
                        throw new IllegalFunctionArgumentException("Expecting '[' instead of '" + pop + "'.");
                    }
                } else if (c == '}') {
                    Character pop = stack.pop();
                    if (pop != '{') {
                        throw new IllegalFunctionArgumentException("Expecting '{' instead of '" + pop + "'.");
                    }
                }
            }
        }

        if (builder != null) {
            list.add(toNode(builder.toString().trim()));
        }

        return list.toArray(new ProcessNode[list.size()]);
    }

    private static ProcessNode toNode(String exp) {
        ProcessNode node;
        if (exp.startsWith("(") && exp.endsWith(")")) {
            node = toFunction("INNER" + exp);

        } else if (!exp.contains("(")) {
            node = new AssignmentNode(exp);

        } else {
            // Function:
            node = toFunction(exp);
        }

        return node;
    }
}
