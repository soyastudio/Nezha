package soya.framework.settler.extractor;

import soya.framework.settler.*;

import java.io.File;

@Component(name="from_file")
public class LocalFileExtractor extends AbstractExtractorBuilder<LocalFileExtractor.Extractor> {

    @Override
    public Extractor build(ProcessNode[] arguments, ProcessSession session) throws ProcessorBuildException {
        AssignmentNode assignment = (AssignmentNode) arguments[0];
        String src = assignment.getStringValue(session.getContext());
        File source = new File(src);

        Extractor extractor = new Extractor(source);
        return extractor;
    }

    static class Extractor extends SourceExtractor<File>{

        protected Extractor(File source) {
            super(source);
        }

        @Override
        protected Object extract(File source) {
            System.out.println("================== extract from file...");
            return null;
        }
    }
}
