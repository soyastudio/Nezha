package soya.framework.nezha.component;

import soya.framework.nezha.*;

public abstract class Extractor implements Processor {
    protected Extractor() {
    }

    @Override
    public void process(ProcessSession session) throws ProcessException {
        session.update(extract());
    }

    protected abstract DataObject extract() throws ExtractException;
}
