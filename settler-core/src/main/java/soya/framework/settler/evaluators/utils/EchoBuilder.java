package soya.framework.settler.evaluators.utils;

import soya.framework.settler.*;

@Component(name = "echo")
public class EchoBuilder implements EvaluatorBuilder<EchoBuilder.Echo> {

    @Override
    public Echo build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException {
        Echo echo = new Echo();
        return echo;
    }

    static class Echo implements Evaluator {

        private Echo() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            System.out.println("========================== !!!");
            return data;
        }
    }
}
