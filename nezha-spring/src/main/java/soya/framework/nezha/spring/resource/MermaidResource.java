package soya.framework.nezha.spring.resource;

import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.parser.XSOMParser;
import io.swagger.annotations.Api;
import org.apache.cxf.common.xmlschema.XmlSchemaUtils;
import org.apache.ws.commons.schema.XmlSchema;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.springframework.stereotype.Component;
import soya.framework.markdown.mermaid.MermaidVisitor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

@Component
@Path("/mermaid")
@Api(value = "Mermaid Service")
public class MermaidResource {

    @POST
    @Path("/flowchart")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_JSON})
    public Response flowChart(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);

        MermaidVisitor visitor = new MermaidVisitor();
        document.accept(visitor);

        return Response.status(200).entity(visitor.getChartList()).build();
    }

    @POST
    @Path("/esql")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response esql(String xsd) throws Exception{

        XSOMParser parser = new XSOMParser();
        parser.parse(new StringReader(xsd));
        XSSchemaSet schemaSet = parser.getResult();

        System.out.println("================= : " +  schemaSet.getSchema(0));


        return Response.status(200).build();
    }
}
