package soya.framework.settler.evaluators.utils;

import soya.framework.settler.*;

@Component(name = "random_number")
public class RandomNumberBuilder implements EvaluatorBuilder<RandomNumberBuilder.RandomNumber> {
    @Override
    public RandomNumber build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        RandomNumber randomNumber = new RandomNumber();
        // TODO:
        return randomNumber;
    }

    static class RandomNumber implements Evaluator {

        private RandomNumber() {
        }

        @Override
        public String evaluate(String data) throws EvaluateException {
            return null;
        }
    }
}
