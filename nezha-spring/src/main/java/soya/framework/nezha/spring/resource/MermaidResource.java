package soya.framework.nezha.spring.resource;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;
import io.swagger.annotations.Api;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;
import soya.framework.markdown.mermaid.MermaidVisitor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Path("/xmlToJson")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response xmlToJson(String xml) throws Exception {

        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        String json = xmlJSONObj.toString(4);
        return Response.status(200).entity(json).build();
    }

    @POST
    @Path("/jsonToEsql")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response jsonToEsql(@HeaderParam("module") String module, String output) throws Exception {
        return Response.status(200).entity(new ModuleBuilder(output).toESQL(module)).build();
    }

    @POST
    @Path("/cmm2esql")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response fromJson(@HeaderParam("module") String module, String xml) throws Exception {
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        String json = xmlJSONObj.toString(4);
        return Response.status(200).entity(new ModuleBuilder(json).toESQL(module)).build();
    }


}
