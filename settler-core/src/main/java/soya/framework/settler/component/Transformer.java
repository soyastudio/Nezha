package soya.framework.settler.component;

import soya.framework.settler.DataObject;
import soya.framework.settler.Processor;

public interface Transformer extends Processor {
    DataObject transform(DataObject in) throws TransformException;
}
