package main;

import com.google.gson.Gson;
import org.glassfish.jersey.internal.util.Base64;
import org.omg.CORBA.SystemException;

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
import java.security.acl.Group;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


class Route {
    String dep;
    String des;
    String train_id;
    String datey;
    String dateh;
    int route_id;

    public Route(String dep, String des, String train_id, String dateh, String datey, int route_id) {
        this.dep = dep;
        this.des = des;
        this.train_id = train_id;
        this.dateh = dateh;
        this.datey = datey;
        this.route_id = route_id;
    }
}

class Ticket {
    String client_id;
    String train_id;
    String dept_station;
    String dest_station;
    String dept_time;
    String dest_time;
    String dept_exact_time;
    String dest_exact_time;

    public Ticket(String client_id, String train_id, String dept_station, String dest_station, String dept_time, String dest_time, String dept_exact_time, String dest_exact_time) {
        this.client_id = client_id;
        this.train_id = train_id;
        this.dept_station = dept_station;
        this.dest_station = dest_station;
        this.dept_time = dept_time;
        this.dest_time = dest_time;
        this.dept_exact_time = dept_exact_time;
        this.dest_exact_time = dest_exact_time;
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
    DataOutputStream dout;
    DataInputStream din;


    public RailwayService() throws IOException {
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

        String url = "jdbc:mysql://localhost:3306/javabase?" + "allowPublicKeyRetrieval=true&useSSL=false";
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
            Socket socket = new Socket("localhost", 2004);

            this.dout = new DataOutputStream(socket.getOutputStream());
            this.din = new DataInputStream(socket.getInputStream());
        }
        catch(Exception e){
            e.printStackTrace();}



        try {
            System.out.println("Database connected!");

            File initialFile = new File("/home/stayal0ne/swe/Karina/railway_project/src/project.sql");

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

    @GET
    @Path("{depart}/{dest}/{date}/{red}/{route}")
    public Response getData1(@PathParam("depart") String depart,
                             @PathParam("dest") String dest,
                             @PathParam("date") String datey,
                             @PathParam("red") String dateh,
                             @PathParam("route") int route) throws IOException {

        String departTemp = depart;
        String destTemp = dest;

        List<Route> params = new ArrayList();
        String s = "";
        try {
            Statement st = connection.createStatement();
            System.out.println(route+"GFDFGHJNHD");
            ResultSet res = st.executeQuery("select distinct s1.name, s2.name, s.exact_timei, s.exact_timef\n" +
                    "from schedule s, station s1, station s2 where s1.id = s.station_i and s2.id = s.station_f and \n" +
                    "s.route_id =" + route + " and s.departure_time =" + datey + "");

            System.out.println("SSSSSSSSSSSSSSSSS");
            while (res.next()) {
                Route r = new Route(res.getString(1), res.getString(2), "blabla",
                        res.getString(3), res.getString(4), route);
                params.add(r);
                s += res.getString(1) + ", ";
                System.out.println(s);
            }
            s += depart + ", ";
            s += dest;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dout.writeUTF(s);
        dout.flush();

        System.out.println("send first mess");
        String str = din.readUTF();//in.readLine();

        System.out.println(str);

        //System.out.println("Message"+ str);

        //WriteToFile(str, "Abyl111.html");
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/stayal0ne/swe/Karina/railway_project/web/map.html"));
        writer.write(str);

        writer.close();
        //System.out.println("Messagerrrrrrrrrrrrrrrrrrrrrrrrr");


        Gson gson = new Gson();
        return Response.ok(gson.toJson(str)).build();
    }

    @GET
    @Path("{depart}/{dest}/{date}")
    public Response getData(@PathParam("depart") String depart,
                            @PathParam("dest") String dest,
                            @PathParam("date") String date) {

        String departTemp = depart;
        String destTemp = dest;
        depart = '"' + depart + '"';
        dest = '"' + dest + '"';
        date = '"' + date + '"';

        List<Route> params = new ArrayList();
        //System.out.println("AAAAAAAAA");
        try {
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("select * from (select distinct t2.name1 as d, t1.name1 as f, s1.exact_timei, s2.exact_timef, s2.route_id from schedule s1, schedule s2, station d1, station d2, train t1, train t2 where  d1.name = " + depart + " and s1.departure_time = " + date + "  and d1.id = s1.station_i and s1.train_id = t1.id and d2.id = s2.station_f  and d2.name = " + dest + " and s2.train_id = t2.id) t where t.d = t.f");

            while (res.next()) {
                Route route = new Route(departTemp, destTemp, res.getString(1), res.getString(3), date, res.getInt(5));
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


//    @Context
//    ServletContext servletContext;
//
//
//    @Path("userProfile")
//    @GET
//    public InputStream getFile(@QueryParam("auth") String token) {
//        try {
//            String base = servletContext.getRealPath("/profile.html");
//            File f = new File(String.format("%s", base));
//            return new FileInputStream(f);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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


    @GET
    @Path("secured/login")
    @Produces("text/html")
    public Response redirect(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
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
