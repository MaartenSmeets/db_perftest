package nl.amis.smeetsm;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.axle.pgclient.PgPool;
import io.vertx.axle.sqlclient.Row;
import io.vertx.axle.sqlclient.RowSet;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@ApplicationScoped
public class DatabaseConfiguration {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfiguration.class.getCanonicalName());
    @Inject
    PgPool client;
    @Inject
    @ConfigProperty(name = "myapp.schema.create", defaultValue = "true")
    boolean schemaCreate;

    @PostConstruct
    void config() {
        if (schemaCreate) {
            initdb();
        }
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private void initdb() {
        System.out.println("InitDB");
        ClassLoader classLoader = getClass().getClassLoader();
        String data = "";
        try {
            InputStream inputStream = classLoader.getResourceAsStream("schema.sql");

            data = readFromInputStream(inputStream);
        } catch (Exception e) {
            data = "";
        }
        if (data != null && !data.equals("")) {
            for (String line : data.split("\\r?\\n")) {
                LOGGER.info("Executing line: " + line);
                if (line.substring(line.length() - 1).equals(";")) {
                    line = line.substring(0, line.length() - 1);
                }

                final CompletionStage<RowSet<Row>> query = client.query(line.trim());
                query.exceptionally(exception -> {
                    LOGGER.info(exception.getMessage());
                    return null;
                });
                query.toCompletableFuture().join();
            }
        }
    }
}
