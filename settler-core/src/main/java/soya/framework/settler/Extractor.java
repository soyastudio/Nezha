package soya.framework.settler;

@ComponentType
public interface Extractor extends Processor {
    DataObject extract();
}
