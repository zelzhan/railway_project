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
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.cert.CertPathChecker;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static main.SqlUtils.*;
import static main.Utils.*;

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
            st.executeUpdate("Insert into message(manager_id, IssueDate, msg) values((select id from registered_user where login ="+"'"+login+"'"+"), now(),"+"'"+message+"'"+")");

        } catch (SQLException e) {
            e.printStackTrace();}

        return Response.ok().build();
    }

    @GET
    @Path("/secured/listOfEmployees")
    public Response allEmployees(ContainerRequestContext requestContext,@Context HttpHeaders headers, @Context ServletContext servletContext) {

        List<Agent> result = findAllEmployees(connection);
        Gson gson = new Gson();
        String authToken = getTokenFromHeader(requestContext);
        authToken = authToken.split(" ")[1];
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());

        return Response.ok(gson.toJson(result)).build();
    }

    @GET
    @Path("/secured/payCheckList/{email}")
    public Response paycheckList(@PathParam("email") String email) {
        findAllPaychecks(connection, email);
        return Response.ok().build();
    }

    @GET
    @Path("/secured/listOfTrains")
    public Response allTrains(@Context HttpHeaders headers,@Context ContainerRequestContext requestContext, @Context ServletContext servletContext) {

        List<ArrayList<String>> result = findAllTrains(connection);
        Gson gson = new Gson();
        String authToken = getTokenFromHeader(requestContext);
        authToken = authToken.split(" ")[1];
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());

        return Response.ok(gson.toJson(result)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/updateSchedule")
    public Response updateScheduleManager(@FormParam("authToken") String authToken, @FormParam("schedule") String schedule,
                                          @Context HttpHeaders headers, @Context ServletContext servletContext,
                                          ContainerRequestContext requestContext, @FormParam("agentEmail") String agentEmail) {
        updateSchedule(connection, authToken, schedule, agentEmail);
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());

        return Response.ok().build();
    }

    @POST
    @Path("/payroll/{login}/{salary}")
    public Response makePayroll(@Context ContainerRequestContext requestContext, @Context ServletContext servletContext,
                                @Context HttpHeaders headers, @PathParam("login") String login,
                                @PathParam("salary") String salary) {
        updateSalaryHistory(connection, login, salary);
        String authToken = getTokenFromHeader(requestContext);
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());

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
    @Path("/createRoute")
    public Response createRoute(String js) throws ParseException {
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


            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date tempDate = formatter.parse(date);
            Date currentDate = Date.from(tempDate.toInstant().plusMillis(TimeUnit.HOURS.toMillis(3)));
            DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
            date = dateFormat.format(currentDate);

        }

        return Response.ok().build();
    }

    @POST

    @Path("cancelRoute/{train_name}/{start_station}/{date}/{hours}/{end_station}")
    public Response cancelRoute(@PathParam("train_name") String train_name,
                                @PathParam("start_station") String start_station,
            @PathParam("date") String date, @PathParam("hours") String hours, @PathParam("end_station") String end_station,
            @Context HttpHeaders headers, @Context ServletContext servletContext, ContainerRequestContext
            requestContext){
        deleteRoute(connection, train_name, start_station, date, hours);
        String authToken = getTokenFromHeader(requestContext);
        authToken = authToken.split(" ")[1];
        String email = getEmailFromToken(authToken);
        makeLog(headers,"User with email " + email, "POST", servletContext,requestContext.getUriInfo().getPath());
        deleteTicketfromRoute(connection, train_name, start_station,end_station);

        return Response.ok().build();
    }


    @POST
    @Path("/adjustHours/{login}/{hours}")
    public Response adjustHours(@PathParam("hours") String hours, @PathParam("login") String login) {

        adjustHoursSql(connection, hours, login);
        return Response.ok().build();
    }

}
