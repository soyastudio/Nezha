package soya.framework.nezha.spring.resource;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleBuilder {
    public static final String DOCUMENT_DATA = "DocumentData";

    private String name;
    private NodeBuilder documentBuilder;
    private NodeBuilder dataObjectBuilder;

    public ModuleBuilder(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        Map.Entry<String, JsonElement> entry = root.entrySet().iterator().next();
        this.name = entry.getKey();
        accept(entry.getValue());
    }

    public void accept(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            jsonElement.getAsJsonObject().entrySet().forEach(e -> {
                if (e.getValue() != null && e.getValue().isJsonObject()) {
                    String objName = e.getKey();
                    JsonObject obj = e.getValue().getAsJsonObject();

                    NodeBuilder objectBuilder = new NodeBuilder(e.getKey(), name);
                    objectBuilder.accept(e.getValue());

                    if (DOCUMENT_DATA.equals(objName)) {
                        this.documentBuilder = objectBuilder;
                    } else {
                        this.dataObjectBuilder = objectBuilder;
                    }
                }
            });
        }
    }

    public String toESQL(String moduleName) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE COMPUTE MODULE ").append(moduleName);
        Utils.nextLine(builder);
        Utils.nextLine(builder);

        printMainFunction(builder);


        builder.append("END MODULE;");
        return builder.toString();
    }

    private void printMainFunction(StringBuilder builder) {
        Utils.indent(builder);
        builder.append("CREATE FUNCTION Main() RETURNS BOOLEAN");
        Utils.nextLine(builder);
        begin(builder, 1);

        Utils.indent(builder, 2);
        builder.append("CREATE LASTCHILD OF OutputRoot DOMAIN ").append("'XMLNSC'").append(";\n");
        Utils.nextLine(builder);

        declareNamespace(builder);
        declareInputVariables(builder);
        declareOutputVariables(builder);

        printTransform(builder);

        Utils.end(builder, 1);
    }

    private void begin(StringBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append("\t");
        }
        builder.append("BEGIN").append("\n");
    }

    private void declareNamespace(StringBuilder builder) {
        Utils.indent(builder, 2);
        builder.append("-- Declare Namespace").append("\n");
        Utils.indent(builder, 2);
        builder.append("DECLARE ").append("Abs").append(" NAMESPACE ").append("'https://collab.safeway.com/it/architecture/info/default.aspx'").append(";").append("\n");

        Utils.nextLine(builder);
    }

    private void declareInputVariables(StringBuilder builder) {
        Utils.indent(builder, 2);
        builder.append("-- Declare Variables for Input Message").append("\n");

        Utils.indent(builder, 2);
        builder.append("DECLARE _row REFERENCE TO InputRoot.XMLNSC.ROWSET.ROW;").append("\n");
        Utils.nextLine(builder);
    }

    private void declareOutputVariables(StringBuilder builder) {

        Utils.indent(builder, 2);
        builder.append("-- Declare Document Variables for Output Message").append("\n");
        declareOutVar(documentBuilder, builder);

        Utils.nextLine(builder);

        Utils.indent(builder, 2);
        builder.append("-- Declare Data Variables for Output Message").append("\n");

        declareOutVar(dataObjectBuilder, builder);
        Utils.nextLine(builder);
    }

    private void declareOutVar(NodeBuilder objectBuilder, StringBuilder builder) {
        String pathExp = objectBuilder.path.replaceAll("/", ".");

        Utils.indent(builder, 2);
        builder.append("DECLARE ").append(objectBuilder.var).append(" REFERENCE TO OutputRoot.XMLNSC.").append(pathExp).append(";\n");
        objectBuilder.children.forEach(e -> {
            if(NodeType.FOLDER.equals(e.type)) {
                declareOutVar(e, builder);
            }
        });
    }

    private void printTransform(StringBuilder builder) {
        Utils.indent(builder, 2);
        builder.append("-- Document Model Mapping").append("\n");
        Utils.indent(builder, 2);
        builder.append("CREATE LASTCHILD OF OutputRoot.XMLNSC")
                .append(" AS ")
                .append(documentBuilder.var)
                .append(" TYPE XMLNSC.Folder NAME '")
                .append(documentBuilder.name)
                .append("'")
                .append(";\n\n");

        documentBuilder.children.forEach(o -> {
            o.printTransform(builder, documentBuilder);
        });

        Utils.nextLine(builder);

        Utils.indent(builder, 2);
        builder.append("-- Data Model Mapping").append("\n");
        Utils.indent(builder, 2);
        builder.append("WHILE LASTMOVE(_row) DO").append("\n");

        Utils.indent(builder, 3);
        builder.append("CREATE LASTCHILD OF OutputRoot.XMLNSC")
                .append(" AS ")
                .append(dataObjectBuilder.var)
                .append(" TYPE XMLNSC.Folder NAME '")
                .append(dataObjectBuilder.name)
                .append("'")
                .append(";\n\n");

        dataObjectBuilder.children.forEach(o -> {
            o.printTransform(builder, dataObjectBuilder);
        });

        Utils.indent(builder, 3);
        builder.append("MOVE ").append(dataObjectBuilder.var).append(" NEXTSIBLING NAME '").append(dataObjectBuilder.var).append("';").append("\n");

        Utils.indent(builder, 2);
        builder.append("END WHILE;").append("\n");
        Utils.nextLine(builder);

    }

    static class NodeBuilder {
        private NodeType type;
        private String name;
        private String var;
        private String path;
        private List<NodeBuilder> children = new ArrayList<>();
        private String mapping;

        public NodeBuilder(String name, String parent) {
            if (name.startsWith("_")) {
                this.type = NodeType.ATTRIBUTE;
                this.name = name.substring(1);
                this.path = parent + "/@" + this.name;

            } else {
                this.name = name;
                this.path = parent + "/" + name;
            }

            String token = this.name;
            int index = token.indexOf(":");
            if(index > 0) {
                token = token.substring(index + 1);
            }
            this.var = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, token);
        }

        void accept(JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                if (type == null) {
                    type = NodeType.FIELD;
                    mapping = "'" + jsonElement.getAsString() + "'";
                }

            } else if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.get("__text") == null) {
                    type = NodeType.FOLDER;
                    jsonObject.entrySet().forEach(e -> {
                        NodeBuilder builder = new NodeBuilder(e.getKey(), path);
                        builder.accept(e.getValue());

                        children.add(builder);
                    });
                } else {
                    type = NodeType.FIELD;

                }
            }
        }

        void printTransform(StringBuilder builder, NodeBuilder parent) {
            int depth = path.split("/").length;
            if (NodeType.FOLDER.equals(type)) {
                Utils.indent(builder, depth);
                builder.append("-- Generate Node ").append(name).append("\n");
                Utils.indent(builder, depth);
                builder.append("CREATE LASTCHILD OF ")
                        .append(parent.var)
                        .append(" AS ")
                        .append(var)
                        .append(" TYPE XMLNSC.Folder NAME '")
                        .append(name)
                        .append("'")
                        .append(";\n");
                children.forEach(e -> {
                    e.printTransform(builder, this);
                });
                Utils.nextLine(builder);
            } else if (NodeType.FIELD.equals(type)) {
                Utils.indent(builder, depth - 1);
                builder.append("-- Set Field ").append(name).append("\n");

                Utils.indent(builder, depth - 1);
                builder.append("SET ").append(parent.var).append(".(XMLNSC.FIELD)").append(name).append(" = ").append(mapping).append(";").append("\n");
            }

        }
    }

    static enum NodeType {
        FOLDER, FIELD, ATTRIBUTE
    }

    static class attributeBuilder {
        private String name;
        private String var;
        private String path;

    }

    static class SimplePropertyBuilder {
        private String name;
        private String var;
        private String path;

    }

    static class Utils {

        static void end(StringBuilder builder, int indent) {
            for (int i = 0; i < indent; i++) {
                builder.append("\t");
            }
            builder.append("END;\n\n");
        }

        static StringBuilder indent(StringBuilder builder) {
            builder.append("\t");
            return builder;
        }

        static StringBuilder indent(StringBuilder builder, int count) {
            for (int i = 0; i < count; i++) {
                builder.append("\t");
            }
            return builder;
        }

        static StringBuilder nextLine(StringBuilder builder) {
            builder.append("\n");
            return builder;
        }
    }
}
