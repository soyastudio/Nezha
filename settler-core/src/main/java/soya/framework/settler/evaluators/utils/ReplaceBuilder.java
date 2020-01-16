package soya.framework.settler.evaluators.utils;

import soya.framework.settler.*;

@Component(name = "replace")
public class ReplaceBuilder implements EvaluatorBuilder<ReplaceBuilder.Replace> {
    @Override
    public Replace build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        Replace replace = new Replace();
        // TODO
        return replace;
    }

    static class Replace implements Evaluator {
        private Replace() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            return null;
        }
    }
}
