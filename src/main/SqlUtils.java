package main;

import main.wrappers.Route;

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

    public static List<Route> findRoute(String depart, String dest, String date, Connection connection) {
        Statement st;

        List<Route> routes = new ArrayList();
        try {
            st = connection.createStatement();
            ResultSet res = st.executeQuery("select * from (select distinct t2.name1 as d, t1.name1 as f, s1.departure_time, s1.exact_timei, s2.arrival_time, s2.exact_timef, s2.route_id from schedule s1, schedule s2, station d1, station d2, train t1, train t2 where  d1.name = \"" + depart + "\" and s1.departure_time = \"" + date + "\"  and d1.id = s1.station_i and s1.train_id = t1.id and d2.id = s2.station_f  and d2.name = \"" + dest + "\" and s2.train_id = t2.id) t where t.d = t.f");
            Statement st2 = connection.createStatement();
            while (res.next()) {
                ResultSet train_id = st2.executeQuery("select id from train where name1=\""+res.getString(1)+"\"");
                train_id.next();
                Route route = new Route(depart, dest, train_id.getString(1), res.getString(3)+" "+res.getString(4), res.getString(5)+" "+res.getString(6), res.getInt(7));
                routes.add(route);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }



    public static String findMapRoute(Connection connection, int route, String datey, String depart, String dest, DataInputStream din, DataOutputStream dout) {


        List<Route> params = new ArrayList();
        String s = "";
        String str = null;
        try {
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("select distinct s1.name, s2.name, s.exact_timei, s.exact_timef\n" +
                    "from schedule s, station s1, station s2 where s1.id = s.station_i and s2.id = s.station_f and \n" +
                    "s.route_id =" + route + " and s.departure_time =" + datey + "");
            while (res.next()) {
                Route r = new Route(res.getString(1), res.getString(2), "blabla",
                        res.getString(3), res.getString(4), route);
                params.add(r);
                s += res.getString(1) + ", ";
            }
            s += depart + ", ";
            s += dest;
            dout.writeUTF(s);
            dout.flush();
            str = din.readUTF();
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/stayal0ne/swe/Karina/railway_project/web/map.html"));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;

    }


}
