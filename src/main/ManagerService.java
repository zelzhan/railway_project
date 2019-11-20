package main;

import com.google.gson.Gson;
import main.graph.Graph;
import main.wrappers.Agent;
import main.wrappers.CreateRoute;
import main.wrappers.Paycheck;
import main.wrappers.RouteBuyTicket;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.cert.CertPathChecker;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static main.SqlUtils.*;

@Path("manager")
public class ManagerService extends HttpServlet {
    Connection connection;
    Graph graph;
    public ManagerService (Connection connection, Graph graph){
        this.connection = connection;
        this.graph = graph;
        System.out.println("Manager service activated!");
    }



    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/send_notify")
    public Response postNotification(@FormParam("login") String login, @FormParam("message") String message) {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate("Insert into message(manager_id, IssueDate, msg) values((select id from registered_user where login ="+login+"), now(),"+message+")");

        } catch (SQLException e) {
            e.printStackTrace();}

        return Response.ok().build();
    }

    @GET
    @Path("/secured/listOfEmployees")
    public Response allEmployees() {

        List<Agent> result = findAllEmployees(connection);
        Gson gson = new Gson();
        return Response.ok(gson.toJson(result)).build();
    }

    @GET
    @Path("/secured/paychecklist/{email}")
    public Response paycheckList(@PathParam("email") String email) {

        findAllPaychecks(connection, email);
        Gson gson = new Gson();
        return Response.ok().build();
    }

    @GET
    @Path("/secured/listOfTrains")
    public Response allTrains() {

        List<ArrayList<String>> result = findAllTrains(connection);
        Gson gson = new Gson();
        System.out.println(result);
        return Response.ok(gson.toJson(result)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/updateSchedule")
    public Response updateScheduleManager(@FormParam("authToken") String authToken, @FormParam("schedule") String schedule, @FormParam("agentEmail") String agentEmail) {
        updateSchedule(connection, authToken, schedule, agentEmail);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/{login}/{salary}")
    public Response makePayroll(@FormParam("authToken") String authToken, @FormParam("login") String login,
                                @FormParam("salary") String salary) {

        updateSalaryHistory(connection, authToken, login, salary);
        return Response.ok().build();

    }

    @GET
    @Path("/showRoutes/{dep}/{des}")
    public Response getAllRoutes(@PathParam("dep") String dep, @PathParam("des") String des) {
        Gson gson = new Gson();
        return Response.ok(gson.toJson(graph.getAllPaths(dep, des))).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/secured/createRoute")
    public Response createRoute(String js) {
        Gson gson  = new Gson();
        CreateRoute route = gson.fromJson(js, CreateRoute.class);
        ArrayList<String> stations = route.getStations();

        String date = route.getDepartureTime();

        String station1;
        String station2;
        String train_id;
        String route_id;


        train_id = getTrainIdPlusOne(connection);
        route_id = getRouteIdPlusOne(connection);

        putTrainIdIntoDb(connection, train_id);

        for (int i = 0; i + 1 < stations.size(); i++) {
            station1 = stations.get(i);
            station2 = stations.get(i+1);

            putRouteIntoDb(connection, station1, station2, train_id, route_id, date);

        }


        return Response.ok().build();
    }

}
