package soya.framework.nezha.spring.configuration;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.Swagger;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import soya.framework.nezha.spring.resource.*;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(GsonMessageBodyHandler.class);

        register(EventBusResource.class);
        register(SchedulerResource.class);
        register(DeploymentResource.class);
        register(PipelineResource.class);

        register(WorkshopResource.class);

        swaggerConfig();
    }

    private Swagger swaggerConfig() {
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);

        BeanConfig swaggerConfigBean = new BeanConfig();
        swaggerConfigBean.setConfigId("Nezha");
        swaggerConfigBean.setTitle("Soya Pipeline Server");
        //swaggerConfigBean.setVersion("v1");
        swaggerConfigBean.setContact("wenqun.soya@gmail.com");
        swaggerConfigBean.setSchemes(new String[]{"http"});
        swaggerConfigBean.setBasePath("/api");
        swaggerConfigBean.setResourcePackage("soya.framework.nezha.spring.resource");
        swaggerConfigBean.setPrettyPrint(true);
        swaggerConfigBean.setScan(true);

        return swaggerConfigBean.getSwagger();
    }
}
