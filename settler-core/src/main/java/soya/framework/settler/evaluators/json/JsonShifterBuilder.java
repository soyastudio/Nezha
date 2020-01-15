package soya.framework.settler.evaluators.json;

import soya.framework.settler.*;
import soya.framework.settler.evaluators.AbstractEvaluatorBuilder;

import java.util.ArrayList;
import java.util.List;

@Component(name = "json_shifter")
public class JsonShifterBuilder extends AbstractEvaluatorBuilder<JsonShifter> {

    @Override
    public JsonShifter build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException {
        JsonShifter evaluator = new JsonShifter();

        List<JsonShifter.Shifter> shifters = new ArrayList<>();
        if(arguments.length == 1 && (arguments[0] instanceof FunctionChainNode)) {

        } else {
            for(ProcessNode node: arguments) {
                FunctionNode func = (FunctionNode) node;
                shifters.add(create(func, context));
            }
        }

        evaluator.shifters = shifters.toArray(new JsonShifter.Shifter[shifters.size()]);

        return evaluator;
    }

    private JsonShifter.Shifter create(FunctionNode func, ProcessContext context) {
        JsonShifter.Shifter sh = new JsonShifter.Shifter();

        for(ProcessNode arg: func.getArguments()) {
            if(sh.to == null) {
                sh.to = ((EvaluateParameter) arg).getStringValue(context);
            } else if(sh.from == null) {
                sh.from = arg;
            }
        }
        return sh;
    }
}
