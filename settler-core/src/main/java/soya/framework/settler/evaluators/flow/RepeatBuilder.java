package soya.framework.settler.evaluators.flow;

import soya.framework.settler.*;

@Component(name = "repeat")
public class RepeatBuilder implements EvaluatorBuilder<RepeatBuilder.Repeat> {

    @Override
    public Repeat build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        Repeat repeat = new Repeat();
        if (arguments[0] instanceof AssignmentNode) {
            repeat.count = AssignmentNode.intValue(arguments[0], session.getContext());
        }

        if (arguments.length > 1) {
            repeat.executor = (Evaluator) Components.create((FunctionNode) arguments[1], session);
        }

        return repeat;
    }

    static class Repeat implements Evaluator {
        private int count;
        private Evaluator executor;

        private Repeat() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            int index = 0;
            while (index < count) {
                executor.evaluate(data);
                index++;
            }

            return null;
        }
    }
}
