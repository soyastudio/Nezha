package soya.framework.markdown.mermaid.model;

import java.util.Objects;

public class MermaidNode {
    private final String name;

    public MermaidNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MermaidNode that = (MermaidNode) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
