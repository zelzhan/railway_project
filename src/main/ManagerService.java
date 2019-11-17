package main;

import com.google.gson.Gson;
import main.graph.Graph;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.SqlUtils.*;
import static main.Utils.getTokenFromHeader;

@Path("manager")
public class ManagerService extends HttpServlet {
    private Graph graph;
    Connection connection;

    public ManagerService (Connection connection, Graph graph){
        this.connection = connection;
        this.graph = graph;
        System.out.println("Manager service activated!");
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/getAllRoutes")
    public Response CreateRoute (ContainerRequestContext requestContext, @FormParam("departure") String departure, @FormParam("destination") String destination) {
        String token = getTokenFromHeader(requestContext);

        ArrayList<List<String>> paths = graph.getAllPaths(departure, destination);
        Gson gson = new Gson();

        return Response.ok(gson.toJson(paths)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/managerProfile")
    public Response managerProfile(@FormParam("authToken") String authToken) {
        return getManagerProfile(connection, authToken);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/updateSchedule")
    public Response updatScheduleManager(@FormParam("authToken") String authToken, @FormParam("schedule") String schedule, @FormParam("agentEmail") String agentEmail) {
        updateSchedule(connection, authToken, schedule, agentEmail);
        return Response.ok().build();
    }
}
