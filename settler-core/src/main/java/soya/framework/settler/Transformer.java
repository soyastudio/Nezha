package soya.framework.settler;

public interface Transformer extends Processor {
    DataObject transform(DataObject in) throws TransformException;
}
