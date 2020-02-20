package soya.framework.markdown.mermaid;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;
import soya.framework.markdown.mermaid.model.FlowChart;

import java.util.ArrayList;
import java.util.List;

public class MermaidVisitor extends AbstractVisitor {
    public static final String MERMAID = "mermaid";

    private List<FlowChart> chartList = new ArrayList<>();

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        if(fencedCodeBlock.getInfo() != null && fencedCodeBlock.getInfo().startsWith(MERMAID)) {
            System.out.println(fencedCodeBlock.getInfo());
            chartList.add(FlowChart.fromCodeBlock(fencedCodeBlock.getLiteral()));
        }
    }

    public List<FlowChart> getChartList() {
        return chartList;
    }
}
