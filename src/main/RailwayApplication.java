package main;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/services")
public class RailwayApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public RailwayApplication() {
        singletons.add(new RailwayService());
        singletons.add(new SecuredService());
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(SecureFilter.class);
        return classes;
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}