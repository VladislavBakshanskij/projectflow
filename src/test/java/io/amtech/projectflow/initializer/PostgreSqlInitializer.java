package io.amtech.projectflow.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgreSqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final JdbcDatabaseContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        POSTGRE_SQL_CONTAINER.start();

        final String jdbcUrl = POSTGRE_SQL_CONTAINER.getJdbcUrl();
        final String username = POSTGRE_SQL_CONTAINER.getUsername();
        final String password = POSTGRE_SQL_CONTAINER.getPassword();

        TestPropertyValues.of("spring.datasource.url=" + jdbcUrl + "?currentSchema=pf",
                              "spring.datasource.username=" + username,
                              "spring.datasource.password=" + password,
                              "spring.liquibase.url=" + jdbcUrl,
                              "spring.liquibase.user=" + username,
                              "spring.liquibase.password=" + password)
                .applyTo(applicationContext);
    }
}
