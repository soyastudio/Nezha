package soya.framework.settler.evaluators.utils;

import soya.framework.settler.*;
import soya.framework.settler.evaluators.AbstractEvaluatorBuilder;

@Component(name="current_timestamp", arguments = "format")
public final class CurrentTimestampBuilder extends AbstractEvaluatorBuilder<CurrentTimestampBuilder.CurrentTimestamp> {

    @Override
    public CurrentTimestamp build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException {
        CurrentTimestamp evaluator = new CurrentTimestamp();
        return evaluator;
    }

    static class CurrentTimestamp implements Evaluator {
        private CurrentTimestamp() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            return "" + System.currentTimeMillis();
        }
    }
}
