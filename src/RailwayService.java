import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class RailwayService {

    public RailwayService() {
    }

    @GET
    @Path("{depart}/{dest}/{time}")
    public Response getData(@PathParam("depart") String depart,
                            @PathParam("dest") String dest,
                            @PathParam("time") int time) {


        // get the list of items from Database

        List<Object> params = new ArrayList();
        params.add(depart);
        params.add(dest);
        params.add(time);
        Gson gson = new Gson();

        return Response.ok(gson.toJson(params)).build();
    }
    @GET
    public Response getSmth(){
        Gson gs = new Gson();
        return Response.ok(gs.toJson("heelo")).build();
    }


}