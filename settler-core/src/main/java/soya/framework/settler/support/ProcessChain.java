package soya.framework.settler.support;

import com.google.common.collect.ImmutableList;
import soya.framework.settler.ProcessException;
import soya.framework.settler.ProcessSession;
import soya.framework.settler.Processor;

import java.util.ArrayList;
import java.util.List;

public class ProcessChain implements Processor {

    private ImmutableList<Processor> chain;

    private ProcessChain(List<Processor> chain) {
        this.chain = ImmutableList.copyOf(chain);
    }

    @Override
    public void process(ProcessSession session) throws ProcessException {
        chain.forEach(e -> {
            e.process(session);
        });
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Processor> chain = new ArrayList<>();

        public Builder add(Processor processor) {
            chain.add(processor);
            return this;
        }

        public ProcessChain create() {
            return new ProcessChain(chain);
        }
    }
}
