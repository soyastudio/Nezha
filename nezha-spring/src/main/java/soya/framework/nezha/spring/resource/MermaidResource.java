package soya.framework.nezha.spring.resource;

import io.swagger.annotations.Api;
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
}
