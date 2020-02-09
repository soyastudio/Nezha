package soya.framework.nezha.component;

import soya.framework.nezha.DataObject;
import soya.framework.nezha.Processor;

public interface Transformer extends Processor {
    DataObject transform(DataObject in) throws TransformException;
}
