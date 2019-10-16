import com.google.gson.Gson;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


@Path("/items")
public class RailwayService extends HttpServlet {

    public RailwayService() {
    }

    @GET
    @Path("{depart}/{dest}/{time}")
    public Response getData(@PathParam("depart") String depart,
                            @PathParam("dest") String dest,
                            @PathParam("time") int time) {


        // get the list of items from Database

        List<Object> params = new ArrayList();
        params.add(depart);
        params.add(dest);
        params.add(time);
        Gson gson = new Gson();

        return Response.ok(gson.toJson(params)).build();
    }
    @GET
    public Response getSmth(){
        Gson gs = new Gson();
        return Response.ok(gs.toJson("heelo")).build();
    }

    @GET
    @Path("/redirect")
    @Produces("text/html")
    public Response redirect(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        String myJsfPage = "/index.html";
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + myJsfPage);
        return Response.status(Response.Status.ACCEPTED).build();
    }


}