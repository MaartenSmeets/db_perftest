package nl.amis.smeetsm;

import io.vertx.axle.pgclient.PgPool;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.concurrent.CompletionStage;

@Path("people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    PgPool client;

    @GET
    public CompletionStage<Response> get() {
        return Person.findAll(client)
                .thenApply(Response::ok)
                .thenApply(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public CompletionStage<Response> getSingle(@PathParam("id") Long id) {
        return Person.findById(client, id)
                .thenApply(person -> person != null ? Response.ok(person) : Response.status(Response.Status.NOT_FOUND))
                .thenApply(Response.ResponseBuilder::build);
    }

    @POST
    public CompletionStage<Response> create(Person person) {
        return person.save(client)
                .thenApply(id -> URI.create("/people/" + id))
                .thenApply(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> delete(@PathParam("id") Long id) {
        return Person.delete(client, id)
                .thenApply(deleted -> deleted ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND)
                .thenApply(status -> Response.status(status).build());
    }
}