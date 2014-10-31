package fr.mmarie.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by Maximilien on 31/10/2014.
 */
@Path("test")
public class TestResource {

    @GET
    public String test() {
        return "test";
    }
}
