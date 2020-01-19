package soya.framework.settler.extractor;

import soya.framework.settler.DataObject;

public abstract class SourceExtractor<T> extends AbstractExtractor {
    private final T source;
    protected String contentType;

    protected SourceExtractor(T source) {
        this.source = source;
    }

    @Override
    public DataObject extract() {
        return DataObject.newInstance(extract(source));
    }

    protected abstract Object extract(T source);
}
