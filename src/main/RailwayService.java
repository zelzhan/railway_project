package main;

import com.google.gson.Gson;
import org.glassfish.jersey.internal.util.Base64;
import org.omg.CORBA.SystemException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.security.acl.Group;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


class Route {
    String dep;
    String des;
    String train_id;
    String date;

    public Route(String dep, String des, String train_id, String date) {
        this.dep = dep;
        this.des = des;
        this.train_id = train_id;
        this.date = date;
    }
}

class Ticket {
    String id;
    String train_id;
    String dept_station;
    String dest_station;
    String dept_time;
    String dest_time;

    public Ticket(String id, String train_id, String dept_station, String dest_station, String dept_time, String dest_time) {
        this.id = id;
        this.train_id = train_id;
        this.dept_station = dept_station;
        this.dest_station = dest_station;
        this.dept_time = dept_time;
        this.dest_time = dest_time;
    }

}




class Passenger {
    String first_name;
    String last_name;
    String phone;
    String email;

    ArrayList<Ticket> past;
    ArrayList<Ticket> future;

    public Passenger(String first_name, String last_name, String phone, String email, ArrayList<Ticket> past, ArrayList<Ticket> future) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.past = past;
        this.future = future;
    }
}

@Path("/items")
public class RailwayService extends HttpServlet {
    Graph graph;
    Connection connection;

    public RailwayService() {
        graph = new Graph();
        graph.addVertex("6");
        graph.addVertex("5");
        graph.addVertex("2");
        graph.addVertex("4");
        graph.addVertex("3");
        graph.addVertex("8");
        graph.addVertex("1");
        graph.addVertex("3");
        graph.addVertex("7");
        graph.addVertex("10");
        graph.addVertex("9");

        graph.addEdge("6", "5");
        graph.addEdge("5", "2");
        graph.addEdge("2", "4");
        graph.addEdge("4", "3");
        graph.addEdge("3", "7");
        graph.addEdge("3", "1");
        graph.addEdge("6", "9");
        graph.addEdge("9", "10");
        graph.addEdge("10", "1");
        graph.addEdge("1", "8");

        graph.printAllPaths("6", "1");

        String url = "jdbc:mysql://localhost:3306/javabase?" + "useSSL=false";
        String username = "java";
        String password = "password";

        System.out.println("Connecting database...");
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Database connected!");

            File initialFile = new File("/Users/demezhanmarikov/IdeaProjects/railway_project_2/src/project.sql");

            try {
                InputStream targetStream = new FileInputStream(initialFile);
                importSQL(connection, targetStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Path("secured/login")
//    public Response login(@FormParam("email") String email, @FormParam("password") String password) {
//
//    }

    @GET
    @Path("{depart}/{dest}/{date}")
    public Response getData(@PathParam("depart") String depart,
                            @PathParam("dest") String dest,
                            @PathParam("date") String date) {


        // get the list of items from Database
        String departTemp = depart;
        String destTemp = dest;
        depart = '"' + depart + '"';
        dest = '"' + dest + '"';
        date = '"' + date + '"';

        List<Route> params = new ArrayList();
        try {
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("select * from (select distinct t2.name1 as d, t1.name1 as f, s1.exact_timei, s2.exact_timef from schedule s1, schedule s2, station d1, station d2, train t1, train t2 where  d1.name = " + depart + " and s1.departure_time = " + date + "  and d1.id = s1.station_i and s1.train_id = t1.id and d2.id = s2.station_f  and d2.name = " + dest + " and s2.train_id = t2.id) t where t.d = t.f");

            while (res.next()) {
                Route route = new Route(departTemp, destTemp, res.getString(1), res.getString(3));
                System.out.println(route.train_id);
                params.add(route);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        return Response.ok(gson.toJson(params)).build();
    }

    //User registration
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
            System.out.println("sfcsdkmj");
            if (res.getString(1).equals("0")) { //sql query to insert an email and password of the new user

                st.executeUpdate("INSERT INTO registered_user (login, first_name, last_name, password, phone) VALUES ( '" + email + "', '" + firstName + "', '" + lastName + "', '"  + password +  "', '" + phone + "')");
            } else {
                return Response.status(Response.Status.CONFLICT).build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

    //USER's PROFILE
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
            ResultSet prevT = st2.executeQuery("select t.id, t.train_id,  t.start_station_id, t.end_station_id, t.departure_time, t.arrival_time  from registered_user u, ticket t where u.login = \"" + email + "\" and t.client_id=u.id and t.departure_time < now()");
            //sql query for getting tickets future the given Currentdate
            ResultSet nextT = st3.executeQuery("select t.id, t.train_id,  t.start_station_id, t.end_station_id, t.departure_time, t.arrival_time  from registered_user u, ticket t where u.login = \"" + email + "\" and t.client_id=u.id and t.departure_time >  now()");

            ArrayList<Ticket> past = new ArrayList<>();
            ArrayList<Ticket> future = new ArrayList<>();

            while (prevT.next()) {
                past.add(new Ticket(prevT.getString(1), prevT.getString(2), prevT.getString(3), prevT.getString(4), prevT.getString(5), prevT.getString(6)));
            }

            while (nextT.next()) {
                future.add(new Ticket(nextT.getString(1), nextT.getString(2), nextT.getString(3), nextT.getString(4), nextT.getString(5), nextT.getString(6)));
            }


            Passenger user = new Passenger(res.getString(1), res.getString(2), res.getString(3), res.getString(4), past, future);

            Gson gson = new Gson();
            return Response.ok(gson.toJson(user)).build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("secured/login")
    @Produces("text/html")
    public Response redirect(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @GET
    @Path("login")
    @Produces("text/html")
    public Response Ruredirect(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {

        String myJsfPage = "/index.html";
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + myJsfPage);
        System.out.println("I'm logged in!");
        return Response.status(Response.Status.ACCEPTED).build();
    }



    public static void importSQL(Connection conn, InputStream in) throws SQLException {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = null;
        try {
            st = conn.createStatement();
            while (s.hasNext()) {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/")) {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            }
        } finally {
            if (st != null) st.close();
        }
    }
}