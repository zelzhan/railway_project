package main;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.StringTokenizer;

@Provider
public class SecureFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
    private static final String SECURED_URL_PREFIX = "secured";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        System.out.println("Filter activated");
        if (!requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)){
            return;
        }

        List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
        if (authHeader != null && authHeader.size() > 0) {
            String authToken = authHeader.get(0);
            authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
            String decodedString = Base64.decodeAsString(authToken);
            StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();

            // check in the dataaase


            String url = "jdbc:mysql://localhost:3306/javabase?" + "useSSL=false";
            String name = "java";
            String pass = "password";

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
                Connection connection = DriverManager.getConnection(url, name, pass);

                Statement st = connection.createStatement();

                ResultSet res = st.executeQuery("select exists(select login from registered_user where login=\""+username+"\" and password=\""+password+"\")" ) ;

                res.next();

                System.out.println(res.getString(1));
                if (res.getString(1).equals("1")) {
                    return;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        Response unauthorizedStatus = Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Don't have rights for this resource")
                .build();


        requestContext.abortWith(unauthorizedStatus);
    }
}
