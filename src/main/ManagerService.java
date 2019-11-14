package main;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;

import static main.SqlUtils.*;
import static main.Utils.*;

@Path("manager")
public class ManagerService extends HttpServlet {
    Connection connection;
    public ManagerService (Connection connection){
        this.connection = connection;
        System.out.println("Manager service activated!");
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/managerProfile")
    public Response managerProfile(@FormParam("authToken") String authToken, @Context HttpHeaders headers) {

        String email = getEmailFromToken(authToken);
        makeLog(headers,"Manager with email "+email, "POST");
        return getManagerProfile(connection, authToken);

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/updateSchedule")
    public Response updatScheduleManager(@FormParam("authToken") String authToken, @FormParam("schedule") String schedule, @FormParam("agentEmail") String agentEmail,
                                         @Context HttpHeaders headers) {

        String email = getEmailFromToken(authToken);
        makeLog(headers,"Manager with email "+email, "POST");
        updateSchedule(connection, authToken, schedule, agentEmail);
        return Response.ok().build();
    }



}
