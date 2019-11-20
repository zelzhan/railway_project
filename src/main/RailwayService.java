package main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Preconditions;
import javafx.util.Pair;
import jdk.nashorn.internal.parser.JSONParser;
import main.graph.Graph;
import main.wrappers.*;
import org.glassfish.jersey.internal.util.Base64;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import static main.SqlUtils.*;
import static main.Utils.*;

@Path("/items")
public class RailwayService extends HttpServlet {
    Graph graph;
    Connection connection;
    DataOutputStream dout;
    DataInputStream din;
    boolean initialized;

    RailwayService(Connection connection, Graph graph){
        this.graph = graph;
        this.initialized = false;
        this.connection = connection;
        System.out.println("Railway is activated!");
    }

    @GET
    @Path("initialize")
    public Response init(@Context ServletContext servletContext) {

        if (!this.initialized){
            Pair<DataInputStream, DataOutputStream> pair = initializeSocket(servletContext, this.dout, this.din);
            this.din = pair.getKey();
            this.dout = pair.getValue();
            this.initialized = true;
        }
        return Response.ok().build();
    }

    @GET
    @Path("getRole")
    public Response getRole(@Context ContainerRequestContext requestContext) {
        String authToken = getTokenFromHeader(requestContext);
        authToken = authToken.split(" ")[1];
        String email = getEmailFromToken(authToken);
        String role = getRoleFromEmail(connection, email);
        Gson gson = new Gson();
        return Response.ok(gson.toJson(role)).build();
    }

    @GET
    @Path("existsEmail/{email}")
    public Response existsEmail(@PathParam("email") String email) {
        Gson gson = new Gson();
        if(emailExists(connection, email)){
            return Response.ok(gson.toJson("true")).build();
        }else{
            return Response.ok(gson.toJson("false")).build();
        }
    }

    @GET
    @Path("{depart}/{dest}/{date}/{red}/{route}")
    public Response getMapData(@PathParam("depart") String depart,
                             @PathParam("dest") String dest,
                             @PathParam("date") String datey,
                             @PathParam("red") String dateh,
                             @PathParam("route") int route,
                               ContainerRequestContext requestContext,
                               @Context HttpHeaders headers,
                               @Context ServletContext servletContext) {

        String result = findMapRoute(connection, route, datey, depart, dest, this.din, this.dout);
        Gson gson = new Gson();
        List<String> authHeader = requestContext.getHeaders().get("Authorization");
        if (authHeader == null) {
            makeLog(headers, "Unauthorized user ", "GET", servletContext, requestContext.getUriInfo().getPath());
        }else{
            String authToken = getTokenFromHeader(requestContext);
            authToken = authToken.split(" ")[1];
            String email = getEmailFromToken(authToken);
            makeLog(headers, "Passenger with email "+ email, "GET",servletContext, requestContext.getUriInfo().getPath());
        }
        return Response.ok(gson.toJson(result)).build();
    }

    @GET
    @Path("{depart}/{dest}/{date}")
    public Response getRouteData(@PathParam("depart") String depart,
                            @PathParam("dest") String dest,
                            @PathParam("date") String date,
                                 @Context HttpHeaders headers, ContainerRequestContext requestContext,
                                 @Context ServletContext servletContext) {

        List<Route> params = findRoute(depart, dest, date, connection);
        List<String> authHeader = requestContext.getHeaders().get("Authorization");
        if (authHeader == null) {
            makeLog(headers, "Unauthorized user ", "GET", servletContext, requestContext.getUriInfo().getPath());
        } else {
            String authToken = getTokenFromHeader(requestContext);
            authToken = authToken.split(" ")[1];
            String email = getEmailFromToken(authToken);
            makeLog(headers, "Passenger with email "+ email, "GET", servletContext, requestContext.getUriInfo().getPath());
        }


        Gson gson = new Gson();
        return Response.ok(gson.toJson(params)).build();
    }

    @GET
    @Path("get_notify")
    public Response getNotify(@Context ServletContext servletContext) {
        List<Message> params = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("select u.login, m.IssueDate, m.msg from message m, registered_user u where m.manager_id=u.id and m.IssueDate>now()");
            res.next();

            while (res.next()) {
                Message msg = new Message(res.getString(1), res.getString(2), res.getString(3));
                params.add(msg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        return Response.ok(gson.toJson(params)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("registration")
    public Response postListItem(@FormParam("email") String email, @FormParam("password") String password, @FormParam("phone") String phone,
                                 @FormParam("firstName") String firstName, @FormParam("lastName") String lastName,
                                 @Context HttpHeaders headers, @Context ServletContext servletContext, ContainerRequestContext requestContext) {
        makeLog(headers, "User with email " + email, "POST", servletContext, requestContext.getUriInfo().getPath());
        return register(connection, email, firstName, lastName, password, phone);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/userProfile")
    public Response userProfile(ContainerRequestContext requestContext, @Context HttpHeaders headers, @Context ServletContext servletContext) {
        String authToken = getTokenFromHeader(requestContext);
        authToken = authToken.split(" ")[1];
        String email = getEmailFromToken(authToken);
//        String authToken = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);

        makeLog(headers,"User with email " + email, "POST", servletContext, requestContext.getUriInfo().getPath());
        return getUserProfile(connection, getEmailFromToken(authToken));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("buyTicket")
    public Response postNewTickets(String js, @Context HttpHeaders headers, @Context ServletContext servletContext, @Context ContainerRequestContext requestContext) {
        Gson gson  = new Gson();
        RouteBuyTicket route = gson.fromJson(js, RouteBuyTicket.class);
        buyTicket(connection, route);
        makeLog(headers, "USer with email "+route.getEmail(), "POST", servletContext,requestContext.getUriInfo().getPath());
        return Response.ok().build();
    }

    @POST
    @Path("cancelTicket")
    public Response cancelTicket(@QueryParam("ticket_id") int ticket_id, @Context HttpHeaders headers, @Context ServletContext servletContext, ContainerRequestContext
                                 requestContext){
        deleteTicket(connection, ticket_id);
        String authToken = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
        authToken = authToken.split(" ")[1];
//        String authToken = getTokenFromHeader();
        String email = getEmailFromToken(authToken);
        makeLog(headers,"User with email " + email, "POST", servletContext,requestContext.getUriInfo().getPath());
        return Response.ok().build();
    }

    @GET
    @Path("secured/login")
    @Produces("text/html")
    public Response redirect(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context HttpHeaders headers, ContainerRequestContext requestContext,
                             @Context ServletContext servletContext) {
        String authToken = getTokenFromHeader(requestContext);
        authToken = authToken.split(" ")[1];
        String email = getEmailFromToken(authToken);
        makeLog(headers, "User with email "+email, "POST", servletContext, requestContext.getUriInfo().getPath());
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @GET
    @Path("getLogs")
    @Produces("text/html")
    public Response getLogs(){
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/sunnya/railway_project/logger.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String logs = sb.toString();
            br.close();
            return Response.ok(gson.toJson(logs)).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(gson.toJson(e.getMessage())).build();
        }
    }

    @GET
    @Path("paychecklist/{email}")
    public Response paycheckList(@PathParam("email") String email) {

        findAllPaychecks(connection, email);
        Gson gson = new Gson();
        return Response.ok().build();
    }
}
