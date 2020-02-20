package soya.framework.markdown.mermaid.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FlowChart {
    public static String LINK_TOKEN = "-->";

    private String graph;
    private Set<MermaidNode> nodeSet = new LinkedHashSet<>();
    private Set<MermaidNodeLink> links = new HashSet<>();

    public FlowChart(String graph, Set<MermaidNode> nodeSet, Set<MermaidNodeLink> links) {
        this.graph = graph;
        this.nodeSet = nodeSet;
        this.links = links;
    }

    public static FlowChart fromCodeBlock(String codeBlock) {
        FlowChart.Builder builder = FlowChart.builder();
        String[] lines = codeBlock.split(";");
        for(String line: lines) {
            builder.readLine(line);
        }

        return builder.build();
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String graph;
        private Set<MermaidNode> nodeSet = new LinkedHashSet<>();
        private Set<MermaidNodeLink> links = new HashSet<>();

        private Builder() {
        }

        public Builder readLine(String line) {
            if(line.contains(LINK_TOKEN)) {
                String[] arr = line.trim().split(LINK_TOKEN);
                if(arr.length == 2) {
                    for(String s: arr) {
                        nodeSet.add(new MermaidNode(s));
                    }
                }
            } else if(line.startsWith("graph ")){
                graph = line.substring("graph ".length()).trim();
            }

            return this;
        }

        public FlowChart build() {
            return new FlowChart(graph, nodeSet, links);
        }
    }

}
