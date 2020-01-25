package soya.framework.settler.component;

import soya.framework.settler.*;

@ComponentType
public abstract class Extractor implements Processor {
    protected Extractor() {
    }

    @Override
    public void process(ProcessSession session) throws ProcessException {
        session.update(extract());
    }

    protected abstract DataObject extract();
}
