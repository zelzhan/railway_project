package main;

import com.google.gson.Gson;
import main.wrappers.RouteBuyTicket;
import org.glassfish.jersey.internal.util.Base64;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import static main.SqlUtils.*;
import static main.Utils.*;

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

    public Response agentProfile(@Context HttpHeaders headers, @Context ServletContext servletContext, ContainerRequestContext requestContext) {
        String authToken = getTokenFromHeader(requestContext);
        String email = getEmailFromToken(authToken);

        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());

        return getAgentProfile(connection);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("buyTicketAgent")
    public Response agentNewTickets(String js, @Context HttpHeaders headers, @Context ContainerRequestContext requestContext, @Context ServletContext servletContext) {
        Gson gson  = new Gson();
        RouteBuyTicket route = gson.fromJson(js, RouteBuyTicket.class);
        buyTicket(connection, route);
        String authToken = getTokenFromHeader(requestContext);
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());

        return Response.ok().build();
    }

    @POST
    @Path("/secured/userAgent")
    public Response userAgent(ContainerRequestContext requestContext, @Context HttpHeaders headers, @Context ServletContext servletContext) {
        List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

        String authToken = authHeader.get(0);
        authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());


        return getUserAgentProfile(connection, authToken);
    }

    @POST
    @Path("/secured/getTicket/{ticketID}")
    public Response getUserTicket(ContainerRequestContext requestContext, @PathParam("ticketID") String ticketID,@Context HttpHeaders headers, @Context ServletContext servletContext) {
        List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

        String authToken = authHeader.get(0);
        authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());

        return getTicket(connection, ticketID);
    }

    @GET
    @Path("/secured/paychecklist/{email}")
    public Response paycheckList(@PathParam("email") String email) {

        findAllPaychecks(connection, email);
        Gson gson = new Gson();
        return Response.ok().build();
    }
}

