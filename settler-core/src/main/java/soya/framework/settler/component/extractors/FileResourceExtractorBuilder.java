package soya.framework.settler.component.extractors;

import soya.framework.settler.*;
import soya.framework.settler.component.Extractor;
import soya.framework.settler.support.ProcessorBuilderSupport;

@Component(name = "from_file")
public class FileResourceExtractorBuilder extends ProcessorBuilderSupport<FileResourceExtractorBuilder.FileResourceExtractor> {

    @Override
    public FileResourceExtractor build(ProcessNode[] arguments, ProcessContext context) throws ProcessorBuildException {
        FileResourceExtractor extractor = new FileResourceExtractor();
        return extractor;
    }

    static class FileResourceExtractor extends Extractor {
        protected FileResourceExtractor() {
        }

        @Override
        protected DataObject extract() {
            return null;
        }
    }
}
