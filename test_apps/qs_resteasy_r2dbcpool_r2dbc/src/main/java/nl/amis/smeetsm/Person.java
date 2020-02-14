package nl.amis.smeetsm;

import io.vertx.axle.pgclient.PgPool;
import io.vertx.axle.sqlclient.Row;
import io.vertx.axle.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class Person {

    private long id;
    private String firstName;
    private String lastName;

    public Person(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static CompletionStage<Person> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT id, first_name, last_name  FROM person WHERE id = $1", Tuple.of(id)).thenApply(PgRowSet -> {
            return PgRowSet.iterator();
        }).thenApply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    private static Person from(io.vertx.axle.sqlclient.Row row) {
        return new Person(row.getLong("id"), row.getString("first_name"), row.getString("last_name"));
    }

    public static CompletionStage<List<Person>> findAll(PgPool client) {
        return client.query("SELECT id, first_name, last_name FROM person").thenApply(pgRowSet -> {
            List<Person> list = new ArrayList<Person>(pgRowSet.size());
            for (Row row : pgRowSet) {
                list.add(from(row));
            }
            return list;
        });
    }

    public static CompletionStage<Boolean> delete(PgPool client, Long id) {
        return client.preparedQuery("DELETE FROM person WHERE id = $1", Tuple.of(id))
                .thenApply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CompletionStage<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO person (first_name,last_name) VALUES ($1,$2) RETURNING (id)", Tuple.of(firstName, lastName))
                .thenApply(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

}
