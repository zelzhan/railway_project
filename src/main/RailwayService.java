package main;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Path("/items")
public class RailwayService extends HttpServlet {

    public RailwayService() {
        Graph graph = new Graph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        graph.addVertex("G");
        graph.addVertex("H");
        graph.addVertex("K");

        graph.addEdge("A", "K");
        graph.addEdge("A", "B");
        graph.addEdge("A", "D");
        graph.addEdge("A", "C");
        graph.addEdge("A", "E");
        graph.addEdge("B", "D");
        graph.addEdge("C", "F");
        graph.addEdge("C", "E");
        graph.addEdge("D", "H");
        graph.addEdge("H", "F");
        graph.addEdge("H", "G");
        graph.addEdge("G", "K");

        graph.printAllPaths("A", "G");
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