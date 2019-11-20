package main;

import com.google.gson.Gson;
import main.graph.Graph;
import main.wrappers.Agent;
import main.wrappers.Paycheck;

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
    public Response paycheckList(@FormParam("email") String email) {

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
    @Path("/secured/managerProfile")
    public Response managerProfile(@FormParam("authToken") String authToken) {

        return getManagerProfile(connection, authToken);

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

}
