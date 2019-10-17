import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

public class Test {

    public static void importSQL(Connection conn, InputStream in) throws SQLException
    {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = null;
        try
        {
            st = conn.createStatement();
            while (s.hasNext())
            {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/"))
                {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0)
                {
                    st.execute(line);
                }
            }
        }
        finally
        {
            if (st != null) st.close();
        }
    }

    public static void main(String [ ] args)
    {
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
//        graph.addEdge("1", "8");
//        graph.addEdge("C", "E");
//        graph.addEdge("D", "H");
//        graph.addEdge("H", "F");
//        graph.addEdge("H", "G");
//        graph.addEdge("G", "K");

        graph.printAllPaths("6", "1");
        System.out.println(graph.prevPaths);

        String url = "jdbc:mysql://localhost:3306/javabase";
        String username = "java";
        String password = "password";

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {


            System.out.println("Database connected!");
            File initialFile = new File("/home/stayal0ne/swe/Karina/railway_project/src/project.sql");
            try {
                InputStream targetStream = new FileInputStream(initialFile);
                importSQL(connection, targetStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            Statement st = connection.createStatement();

            ResultSet res = st.executeQuery("select * from schedule");
            while(res.next()) {
                System.out.println(res.getString(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }



    }
}
