package main;

import org.glassfish.jersey.internal.util.Base64;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.List;

import static main.SqlUtils.getAgentProfile;

@Path("agent")
public class AgentService extends HttpServlet {


    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    Connection connection;
    public AgentService (Connection connection){
        this.connection = connection;
        System.out.println("Agent service activated!");
    }

    @POST
    @Path("/secured/agentProfile")
    public Response agentProfile(ContainerRequestContext requestContext) {

        List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);


        String authToken = authHeader.get(0);

        authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");

        return getAgentProfile(connection, authToken);
    }
}

