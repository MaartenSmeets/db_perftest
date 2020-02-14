package nl.amis.smeetsm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public List<Person> get() {
        return Person.findAll().list();
    }

    @GET
    @Path("{id}")
    public Person getSingle(@PathParam("id") Long id) {
        return Person.findById(id);
    }

    @POST
    public void create(Person person) {
        person.persist();
    }

    @DELETE
    @Path("{id}")
    public long delete(@PathParam("id") Long id) {
        return Person.delete("id",id);
    }
}