package main;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("agent")
public class AgentService extends HttpServlet {
    public AgentService (){
        System.out.println("Agent service activated!");
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String secureMethod() {
        return "TEST Agent API";
    }
}
