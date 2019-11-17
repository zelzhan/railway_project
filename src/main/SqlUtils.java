package main;

import com.google.gson.Gson;
import javafx.util.Pair;
import main.wrappers.*;
import org.glassfish.jersey.internal.util.Base64;

import javax.swing.*;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SqlUtils {

    public static String getRoleFromEmail(Connection connection, String email) {
        Statement st = null;
        try {
            st = connection.createStatement();
            String query = "select role from registered_user where login = '"+ email+"'";

            ResultSet id = st.executeQuery(query);
            id.next();
            return id.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

    public static void buyTicket (Connection connection, RouteBuyTicket route) {
        try {
            String email = route.getEmail();
            Statement st2 = connection.createStatement();
            ResultSet id1 = st2.executeQuery("select ID from station where name = \"" +route.getStart_station() + "\"");
            id1.next();
            Statement st3 = connection.createStatement();
            ResultSet id2 = st3.executeQuery("select ID from station where name = \"" +route.getEnd_station()+ "\"");
            id2.next();
            Statement st = connection.createStatement();
            st.executeUpdate("insert into ticket(train_id, start_station_id, end_station_id, departure_time, arrival_time, ReservStatus, client_id) values (" + Integer.parseInt(route.getTrain_id()) + "," + id1.getString(1) + "," + id2.getString(1) +" , \"" + route.getDeptTime() + "\", \"" + route.getDestTime() + "\",\"Booked\" , (select id from registered_user where login = \"" + email + "\"))");
            Statement st4 = connection.createStatement();
            st4.executeUpdate("Update schedule Set availability=availability-1 Where route_id=" + route.getRoute_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTicket(Connection connection, int ticket_id){
        try {
            Statement st = connection.createStatement();
            st.executeUpdate("Update ticket Set ReservStatus = 'Cancelled' Where id="+ticket_id);
            Statement st4 = connection.createStatement();
            st4.executeUpdate("Update schedule set availability = availability +1 where train_id=(select train_id from ticket where id="+ticket_id);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Response getUserProfile (Connection connection, String authToken) {

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
            ResultSet prevT = st2.executeQuery("select distinct t.id, t.train_id,  s1.name, s2.name, t.departure_time, t.arrival_time, t.ReservStatus from registered_user u, ticket t, station s1, station s2 where u.login = \""+email+"\" and s1.id=t.start_station_id and s2.id= t.end_station_id and t.client_id=u.id and t.departure_time < now();");

            //sql query for getting tickets future the given Currentdate
            ResultSet nextT = st3.executeQuery("select distinct t.id, t.train_id,  s1.name, s2.name, t.departure_time, t.arrival_time, t.ReservStatus from registered_user u, ticket t, station s1, station s2 where u.login = \""+email+"\" and s1.id=t.start_station_id and s2.id= t.end_station_id and t.client_id=u.id and t.departure_time > now();");


            ArrayList<Ticket> past = new ArrayList<>();
            ArrayList<Ticket> future = new ArrayList<>();

            while (prevT.next()) {
                past.add(new Ticket(email,prevT.getString(1), prevT.getString(2), prevT.getString(3), prevT.getString(4), prevT.getString(5), prevT.getString(6), prevT.getString(7)));
            }

            while (nextT.next()) {
                future.add(new Ticket(email,nextT.getString(1), nextT.getString(2), nextT.getString(3), nextT.getString(4), nextT.getString(5), nextT.getString(6), nextT.getString(7)));
            }

            Passenger user = new Passenger(res.getString(1), res.getString(2), res.getString(3), res.getString(4), past, future);

            Gson gson = new Gson();
            return Response.ok(gson.toJson(user)).build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateSchedule(Connection connection, String authToken, String new_schedule, String email){
        try {
            Statement st = connection.createStatement();
            st.executeQuery("update  regular_employee set schedule =\""+new_schedule+"\" where login =\""+email+"\"");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateSalaryHistory(Connection connection, String authToken, String employee_id){
        try {
            Statement st = connection.createStatement();
            st.executeQuery("u");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Managers profile returns all info about manager and all agents
    public static Response getManagerProfile(Connection connection, String authToken){

        try{
            String decodedString = Base64.decodeAsString(authToken);
            StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
            String email = tokenizer.nextToken();

            Statement st3 = connection.createStatement();

            //sql query for getting all personal info by email
            ResultSet res1 = st3.executeQuery("select u.first_name, u.last_name, u.phone, u.login from registered_user u where u.login = \"" + email + "\"");
            res1.next();

            Statement st = connection.createStatement();
            ResultSet agents = st.executeQuery("select r.login, u.first_name, u.last_name, s.name as station, r.salary, r.schedule from registered_user u, regular_employee r, station s where s.id=r.stationN and u.id = r.id;");
            ArrayList<Agent> allagents = new ArrayList<>();
            while(agents.next()){
                allagents.add(new Agent(agents.getString(2), agents.getString(3),agents.getString(1), agents.getString(6),agents.getInt(5),agents.getString(4)));
            }
            Pair<Passenger, ArrayList<Agent>> result = new Pair<Passenger, ArrayList<Agent>>( new Passenger(res1.getString(1), res1.getString(2),agents.getString(3), agents.getString(4)), allagents);


            Gson gson = new Gson();
            return Response.ok(gson.toJson(result)).build();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    public static Response getAgentProfile (Connection connection, String authToken) {

        try {
            String decodedString = Base64.decodeAsString(authToken);
            StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
            String email = tokenizer.nextToken();
            Statement st = connection.createStatement();
            Statement st2 = connection.createStatement();
            Statement st3 = connection.createStatement();
            ResultSet tickets = st2.executeQuery("select e.login, t.* from registered_user e, ticket t\n" +
                    "where e.id=t.client_id and  t.departure_time >  now();\n");

            ArrayList<Pair<Passenger, Ticket>> alltickets = new ArrayList<>();

            while (tickets.next()) {
                ResultSet res = st.executeQuery("select u.first_name, u.last_name, u.phone, u.login from registered_user u where u.login = \"" + tickets.getString(1) + "\"");
                res.next();
                alltickets.add(new Pair<>(new Passenger(res.getString(1), res.getString(2), res.getString(3), res.getString(4)),
                        new Ticket(tickets.getString(1),tickets.getString(2), tickets.getString(4), tickets.getString(5), tickets.getString(6), tickets.getString(7), tickets.getString(8), tickets.getString(9))));
            }
            Gson gson = new Gson();
            return Response.ok(gson.toJson(alltickets)).build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response register(Connection connection, String email, String firstName, String lastName, String password, String phone) {
        try {
            //Unique email should be inserted in the database
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT EXISTS (select login from registered_user where login =\"" + email + "\")"); //sql query for checking an email for uniqueness
            res.next();
            if (res.getString(1).equals("0")) { //sql query to insert an email and password of the new user
                st.executeUpdate("INSERT INTO registered_user (login, first_name, last_name, password, phone) VALUES ( '" + email + "', '" + firstName + "', '" + lastName + "', '"  + password +  "', '" + phone + "')");
                Statement st1 = connection.createStatement();
                ResultSet res1 = st1.executeQuery("SELECT EXISTS (select login from registered_user where login =\"" + email + "\")"); //sql query for checking an email for uniqueness
                res1.next();
            } else {
                return Response.status(Response.Status.CONFLICT).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    public static List<Route> findRoute(String depart, String dest, String date, Connection connection) {
        Statement st;

        List<Route> routes = new ArrayList();
        try {
            st = connection.createStatement();
            ResultSet res = st.executeQuery("select * from (select distinct t2.name1 as d, t1.name1 as f, s1.departure_time, s2.arrival_time, s2.route_id, s1.availability from schedule s1, schedule s2, station d1, station d2, train t1, train t2 where  d1.name = \""+depart+"\" and date(s1.departure_time) = \""+date+"\"  and d1.id = s1.station_i and s1.train_id = t1.id and d2.id = s2.station_f  and d2.name = \""+dest+"\" and s2.train_id = t2.id) t where t.d = t.f");
            Statement st2 = connection.createStatement();
            while (res.next()) {
                ResultSet train_id = st2.executeQuery("select id from train where name1=\""+res.getString(1)+"\"");
                train_id.next();
                Route route = new Route(depart, dest, train_id.getString(1), res.getString(3), res.getString(4), res.getInt(5), res.getInt(6));
                routes.add(route);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }

    public static List<Agent> findAllEmployees(Connection connection) {
        Statement st;
        List<Agent> employees = new ArrayList();
        try {
            st = connection.createStatement();
            ResultSet res = st.executeQuery("select u.first_name, u.last_name, e.salary, e.login, e.schedule, e.stationN\n" +
                    "from registered_user u, regular_employee e where\n" +
                    "u.id = e.id ");
            while (res.next()) {
                Agent employee = new Agent(res.getString(1), res.getString(2),
                        res.getString(4), res.getString(5), res.getInt(3), res.getString(6));
                employees.add(employee);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public static String findMapRoute(Connection connection, int route, String datey, String depart, String dest, DataInputStream din, DataOutputStream dout) {

        List<Route> params = new ArrayList();
        String s = "";
        String str = null;
        try {
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("select * from (select distinct t2.name1 as d, t1.name1 as f, s1.departure_time, s2.arrival_time, s2.route_id, s1.availability from schedule s1, schedule s2, station d1, station d2, train t1, train t2 where  d1.name = \""+depart+"\" and date(s1.departure_time) = \""+datey+"\"  and d1.id = s1.station_i and s1.train_id = t1.id and d2.id = s2.station_f  and d2.name = \'"+dest+"\' and s2.train_id = t2.id) t where t.d = t.f");
            while (res.next()) {
                Route r = new Route(res.getString(1), res.getString(2), "blabla",
                        res.getString(3), res.getString(4), route, res.getInt(5));
                params.add(r);
                s += res.getString(1) + ", ";
            }
            s += depart + ", ";
            s += dest;
            dout.writeUTF(s);
            dout.flush();
            str = din.readUTF();
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\abyl\\Desktop\\Fall 2019\\Software Engineering\\SE lab\\lab6\\" +
                    "railway_project-master\\classes\\artifacts\\railway_station_service_war_exploded\\map.html"));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;

    }
}
