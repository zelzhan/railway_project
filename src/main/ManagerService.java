package main;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("manager")
public class ManagerService extends HttpServlet {
    public ManagerService (){
        System.out.println("Manager service activated!");
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String secureMethod() {
        return "TEST Manager API";
    }
}
