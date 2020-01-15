package soya.framework.settler;

@ComponentType()
public interface Evaluator extends Processor {
    String evaluate(String data) throws EvaluateException;
}
