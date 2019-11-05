package main;

import main.security.SecureFilter;
import main.security.SecuredService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import static main.Utils.initializeDatabase;


@ApplicationPath("/services")
public class RailwayApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();
    public static final String PROPERTIES_FILE = "config.properties";
    public static Properties properties = new Properties();
    Connection connection;

    public RailwayApplication(@Context ServletContext servletContext) throws IOException {


        readProperties();

        this.connection = initializeDatabase(this.connection, servletContext);
        singletons.add(new RailwayService(this.connection));
        singletons.add(new SecuredService(this.connection));
        singletons.add(new AgentService(this.connection));
        singletons.add(new ManagerService(this.connection));
    }


    private Properties readProperties() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                // TODO Add your custom fail-over code here
                e.printStackTrace();
            }
        }
        return properties;
    }


    @Override
    public Set<Class<?>> getClasses() {

        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(SecureFilter.class);
        classes.add(AgentService.class);
        classes.add(ManagerService.class);
        classes.add(RailwayService.class);

        return classes;
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
