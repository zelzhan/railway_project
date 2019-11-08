package main;

import javafx.util.Pair;
import main.graph.Graph;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Utils {

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

    public static Connection initializeDatabase(Connection connection, @Context ServletContext servletContext) {

        String url = "jdbc:mysql://localhost:3306/javabase?" + "allowPublicKeyRetrieval=true&useSSL=false";
        String username = RailwayApplication.properties.getProperty("USERNAME");
        String password = RailwayApplication.properties.getProperty("PASSWORD");

        System.out.println("Connecting database...");
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, username, password);

            String path = servletContext.getRealPath("/");
            String pathToSql = new File(path).getParentFile().getParentFile().getParent() + "/project.sql";
            File initialFile = new File(pathToSql);
            InputStream targetStream = new FileInputStream(initialFile);
            importSQL(connection, targetStream);
            System.out.println("Database connected!\n");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static Pair<DataInputStream, DataOutputStream> initializeSocket (@Context ServletContext servletContext, DataOutputStream dout, DataInputStream din) {
        System.out.println("Connecting socket...");
        Pair<DataInputStream, DataOutputStream> pair = new Pair<>(din, dout);
        try {

            // starting new Thread
            String path = servletContext.getRealPath("/");
            String pathToRoot = new File(path).getParentFile().getParentFile().getParent();
            ProcessBuilder builder = new ProcessBuilder(RailwayApplication.properties.getProperty("PYTHON"), pathToRoot + "/mapgenerator.py");
            builder.directory(new File(pathToRoot));
            builder.start();

            // Wait until the python script will run
            TimeUnit.SECONDS.sleep(5);

            // Initialize socket
            int port = Integer.parseInt(RailwayApplication.properties.getProperty("SOCKET_PORT"));
            Socket socket = new Socket("localhost", port);
            pair = new Pair<>(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()));
            System.out.println("Socket connected!\n");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return pair;
    }


    public static Graph initializeGraph() {

        Graph graph = new Graph();
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
        return graph;
    }
}
