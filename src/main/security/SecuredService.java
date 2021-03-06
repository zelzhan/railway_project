package main.security;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;

@Path("secured")
public class SecuredService extends HttpServlet {
    Connection connection;
    public SecuredService (Connection connection){
        this.connection = connection;
        System.out.println("Security is activated!");
    }

    @GET
    @Path("message")
    @Produces(MediaType.TEXT_PLAIN)
    public String secureMethod() {
        return "secured API";
    }
}
