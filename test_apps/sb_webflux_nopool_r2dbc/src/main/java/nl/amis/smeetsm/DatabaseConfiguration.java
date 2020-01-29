package nl.amis.smeetsm;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;

//update with https://github.com/Aleksandr-Filichkin/r2dbc-vs-jdbc/blob/master/r2dbc-service/src/main/java/com/filichkin/blog/db/reactive/SubscriptionConfiguration.java
// https://medium.com/@filia.aleks/r2dbc-vs-jdbc-19ac3c99fafa
@Configuration
@EnableR2dbcRepositories
class DatabaseConfiguration extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.username}")
    String username;

    @Value("${spring.r2dbc.password}")
    String password;

    @Value("${spring.r2dbc.url}")
    String url;

    private static final String DB_PROTOCOL = "postgresql";
    private static final String DB_DRIVER = "pool";
    private int maxClientConnections = 1000;

    @Bean
    public ConnectionFactory connectionFactory() {
        R2DBCURLSplitter myUrl = new R2DBCURLSplitter(url);
        System.out.println(myUrl.toString());
        String host = myUrl.getHost();
        Integer port = myUrl.getPort();
        String database = myUrl.getDatabase();

        //return ConnectionFactories.get(ConnectionFactoryOptions.builder()
        //        .option(DRIVER, DB_DRIVER)
        //        .option(PROTOCOL, DB_PROTOCOL)
        //        .option(MAX_SIZE, maxClientConnections)
        //        .option(HOST, host)
        //        .option(PORT, port)
        //        .option(USER, username)
        //        .option(PASSWORD, password)
        //        .option(DATABASE, database)
        //        .build());
        //unlimited connections below?
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .database(database)
                        .username(username)
                        .password(password)
                        .build()
        );
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        ClassPathResource cpr = new ClassPathResource("schema.sql");
        if (cpr.exists()) {
            populator.addPopulators(new ResourceDatabasePopulator(cpr));
        }
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}
