package soya.framework.settler;

@ComponentType()
public interface Evaluator  {
    String evaluate(String data) throws EvaluateException;
}
