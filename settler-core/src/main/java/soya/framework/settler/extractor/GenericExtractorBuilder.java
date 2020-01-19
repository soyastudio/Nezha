package soya.framework.settler.extractor;

import soya.framework.settler.*;

@Component(name = "from")
public class GenericExtractorBuilder extends AbstractExtractorBuilder<GenericExtractorBuilder.GenericExtractor> {

    @Override
    public GenericExtractor build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        Processor[] processors = new Processor[arguments.length];
        for(int i = 0; i < arguments.length; i ++) {
            //processors[i] = Components.create((ExecutableNode) arguments[i], session);
        }
        GenericExtractor extractor = new GenericExtractor(processors);

        return extractor;
    }

    static class GenericExtractor extends AbstractExtractor {
        private Processor[] extractors;

        private GenericExtractor(Processor[] extractors) {
            this.extractors = extractors;
        }

        @Override
        public DataObject extract() {
            for(Processor extractor: extractors) {
                System.out.println("-------------------- " + extractor.getClass().getName());
            }

            return null;
        }
    }
}
