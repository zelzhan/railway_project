package main;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;

@Path("manager")
public class ManagerService extends HttpServlet {
    Connection connection;
    public ManagerService (Connection connection){
        this.connection = connection;
        System.out.println("Manager service activated!");
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String secureMethod() {
        return "TEST Manager API";
    }
}
