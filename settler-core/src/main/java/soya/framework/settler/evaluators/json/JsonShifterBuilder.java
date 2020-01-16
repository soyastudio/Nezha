package soya.framework.settler.evaluators.json;

import soya.framework.settler.*;
import soya.framework.settler.evaluators.AbstractEvaluatorBuilder;

import java.util.ArrayList;
import java.util.List;

@Component(name = "json_shifter")
public class JsonShifterBuilder implements EvaluatorBuilder<JsonShifter> {

    @Override
    public JsonShifter build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        JsonShifter evaluator = new JsonShifter();

        List<JsonShifter.Shifter> shifters = new ArrayList<>();
        if(arguments.length == 1 && (arguments[0] instanceof ArrayNode)) {

        } else {
            for(ProcessNode node: arguments) {
                FunctionNode func = (FunctionNode) node;
                shifters.add(create(func, session.getContext()));
            }
        }

        evaluator.shifters = shifters.toArray(new JsonShifter.Shifter[shifters.size()]);

        return evaluator;
    }

    private JsonShifter.Shifter create(FunctionNode func, ProcessContext context) {
        JsonShifter.Shifter sh = new JsonShifter.Shifter();

        for(ProcessNode arg: func.getArguments()) {
            if(sh.to == null) {
                sh.to = ((AssignmentNode) arg).getStringValue(context);
            } else if(sh.from == null) {
                sh.from = arg;
            }
        }
        return sh;
    }
}
