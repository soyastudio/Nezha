package soya.framework.settler.support;

import soya.framework.settler.ProcessException;
import soya.framework.settler.ProcessSession;
import soya.framework.settler.ProcessSessionAware;
import soya.framework.settler.Processor;

public class ProcessorSupport implements Processor, ProcessSessionAware {
    protected ProcessSession session;

    protected ProcessorSupport() {
    }

    public ProcessSession getSession() {
        return session;
    }

    @Override
    public void process(ProcessSession session) throws ProcessException {

    }
}
