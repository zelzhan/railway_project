package main;

import com.google.gson.Gson;
import javafx.util.Pair;
import main.graph.Graph;
import main.wrappers.Passenger;
import main.wrappers.Route;
import main.wrappers.Ticket;
import org.glassfish.jersey.internal.util.Base64;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import static main.SqlUtils.findMapRoute;
import static main.SqlUtils.findRoute;
import static main.Utils.*;

@Path("/items")
public class RailwayService extends HttpServlet {
    Graph graph;
    Connection connection;
    DataOutputStream dout;
    DataInputStream din;
    boolean initalized;

    public RailwayService() throws IOException {
        this.graph = initalizeGraph(graph);
        this.initalized = false;

    }

    @GET
    @Path("initialize")
    public Response init(@Context ServletContext servletContext) {

        if (!this.initalized){
            connection = initializeDatabase(connection, servletContext);
            Pair<DataInputStream, DataOutputStream> pair = initializeSocket(servletContext, this.dout, this.din);
            this.din = pair.getKey();
            this.dout = pair.getValue();
            this.initalized = true;
        }
        return Response.ok().build();
    }

    @GET
    @Path("{depart}/{dest}/{date}/{red}/{route}")
    public Response getData1(@PathParam("depart") String depart,
                             @PathParam("dest") String dest,
                             @PathParam("date") String datey,
                             @PathParam("red") String dateh,
                             @PathParam("route") int route) throws IOException {

        List<Route> params = new ArrayList();
        String result = findMapRoute(connection, route, datey, depart, dest, this.din, this.dout);
        Gson gson = new Gson();
        return Response.ok(gson.toJson(result)).build();
    }

    @GET
    @Path("{depart}/{dest}/{date}")
    public Response getData(@PathParam("depart") String depart,
                            @PathParam("dest") String dest,
                            @PathParam("date") String date) {

        List<Route> params = findRoute('"' + depart + '"', '"' + dest + '"', '"' + date + '"', connection, depart, dest);
        Gson gson = new Gson();
        return Response.ok(gson.toJson(params)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("send")
    public Response postListItem(@FormParam("email") String email, @FormParam("password") String password, @FormParam("phone") String phone,
                                 @FormParam("firstName") String firstName, @FormParam("lastName") String lastName) {
        try {
            //Unique email should be inserted in the database
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT EXISTS (select login from registered_user where login =\"" + email + "\")"); //sql query for checking an email for uniqueness
            res.next();
            System.out.println(res.getString(1));
            if (res.getString(1).equals("0")) { //sql query to insert an email and password of the new user
                st.executeUpdate("INSERT INTO registered_user (login, first_name, last_name, password, phone) VALUES ( '" + email + "', '" + firstName + "', '" + lastName + "', '"  + password +  "', '" + phone + "')");
                Statement st1 = connection.createStatement();
                ResultSet res1 = st1.executeQuery("SELECT EXISTS (select login from registered_user where login =\"" + email + "\")"); //sql query for checking an email for uniqueness
                res1.next();
                System.out.println(res1.getString(1));
            } else {
                return Response.status(Response.Status.CONFLICT).build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/secured/userProfile")
    public Response userProfile(@FormParam("authToken") String authToken) {

        try {
            String decodedString = Base64.decodeAsString(authToken);
            StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
            String email = tokenizer.nextToken();
            Statement st = connection.createStatement();
            Statement st2 = connection.createStatement();
            Statement st3 = connection.createStatement();

            //sql query for getting all personal info by email
            ResultSet res = st.executeQuery("select u.first_name, u.last_name, u.phone, u.login from registered_user u where u.login = \"" + email + "\"");
            res.next();
            //sql query for getting tickets past the given Currentdate
            ResultSet prevT = st2.executeQuery("select distinct t.id, t.train_id,  s1.name, s2.name, t.departure_time, t.arrival_time, a1.exact_timei, a2.exact_timef from registered_user u, ticket t, station s1, station s2, schedule a1, schedule a2 where u.login = \"" + email + "\" and s1.id=t.start_station_id and s2.id= t.end_station_id and t.client_id=u.id and a1.station_i=t.start_station_id and a2.station_f=t.end_station_id and t.departure_time < now(); ");
            //sql query for getting tickets future the given Currentdate
//            ResultSet nextT = st3.executeQuery("select t.id, t.train_id,  t.start_station_id, t.end_station_id, t.departure_time, t.arrival_time  from registered_user u, ticket t where u.login = \"" + email + "\" and t.client_id=u.id and t.departure_time >  now()");
            ResultSet nextT = st3.executeQuery("select distinct t.id, t.train_id,  s1.name, s2.name, t.departure_time, t.arrival_time, a1.exact_timei, a2.exact_timef from registered_user u, ticket t, station s1, station s2, schedule a1, schedule a2 where u.login = \"" + email + "\" and s1.id=t.start_station_id and s2.id= t.end_station_id and t.client_id=u.id and a1.station_i=t.start_station_id and a2.station_f=t.end_station_id and t.departure_time > now();");

            ArrayList<Ticket> past = new ArrayList<>();
            ArrayList<Ticket> future = new ArrayList<>();

            while (prevT.next()) {
                past.add(new Ticket(prevT.getString(1), prevT.getString(2), prevT.getString(3), prevT.getString(4), prevT.getString(5), prevT.getString(6), prevT.getString(7), prevT.getString(8)));
            }

            while (nextT.next()) {
                future.add(new Ticket(nextT.getString(1), nextT.getString(2), nextT.getString(3), nextT.getString(4), nextT.getString(5), nextT.getString(6), nextT.getString(7), nextT.getString(8)));
            }

            Passenger user = new Passenger(res.getString(1), res.getString(2), res.getString(3), res.getString(4), past, future);

            Gson gson = new Gson();
            return Response.ok(gson.toJson(user)).build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TICKECTS BUYING
    //User registration
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("buyTicket")
    public Response postNewTickets(@FormParam("authToken") String authToken, @FormParam("train_id") int train_id, @FormParam("start_station_id") int start_station_id,
                                 @FormParam("end_station_id") int end_station_id, @FormParam("destTime") String destTime, @FormParam("deptTime") String deptTime) {
        try {
            String decodedString = Base64.decodeAsString(authToken);
            StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
            String email = tokenizer.nextToken();
            //
            Statement st = connection.createStatement();
            int res =   st.executeUpdate("insert into ticket(train_id, start_station_id, end_station_id, departure_time, arrival_time, availability, client_id) values (\"" + train_id + "\",\"" + start_station_id + "\",\"" + end_station_id + "\",\""+deptTime+"\",\"" +destTime+ "\"," + " -1, (select id from registered_user where login like \""+email+"\"))");
            System.out.println(res);
            if (res == 1){
                return Response.status(Response.Status.OK).build();
            } else{
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

    @GET
    @Path("secured/login")
    @Produces("text/html")
    public Response redirect(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
