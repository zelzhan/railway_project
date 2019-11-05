package main;

import main.security.SecureFilter;
import main.security.SecuredService;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/services")
public class RailwayApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();
    public static final String PROPERTIES_FILE = "config.properties";
    public static Properties properties = new Properties();

    public RailwayApplication() throws IOException {
        singletons.add(new RailwayService());
        singletons.add(new SecuredService());
        singletons.add(new AgentService());
        singletons.add(new ManagerService());
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

        readProperties();

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
