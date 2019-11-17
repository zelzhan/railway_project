package main;

import com.google.gson.Gson;
import main.wrappers.Agent;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public ManagerService (Connection connection){
        this.connection = connection;
        System.out.println("Manager service activated!");
    }



    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/send_notify")
    public Response postNotification(@FormParam("login") String login, @FormParam("login") String date,  @FormParam("message") String message) {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate("Insert into message(manager_id, IssueDate, msg) values((select id from registered_user where login ="+login+"), "+date+","+message+")");

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
    @Path("/secured/payroll")
    public Response makePayroll(@FormParam("authToken") String authToken, @FormParam("employee_id") String employee_id) {

        updateSalaryHistory(connection, authToken, employee_id);
        return Response.ok().build();

    }



}
