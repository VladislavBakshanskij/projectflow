package io.amtech.projectflow.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgreSqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final JdbcDatabaseContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));

    private static final String DATASOURCE_PREFIX = "spring.datasource";
    private static final String LIQUIBASE_PREFIX = "spring.liquibase";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        POSTGRE_SQL_CONTAINER.start();

        final String jdbcUrl = POSTGRE_SQL_CONTAINER.getJdbcUrl();
        final String username = POSTGRE_SQL_CONTAINER.getUsername();
        final String password = POSTGRE_SQL_CONTAINER.getPassword();

        TestPropertyValues.of(DATASOURCE_PREFIX + ".url=" + jdbcUrl + "?currentSchema=pf",
                        DATASOURCE_PREFIX + ".username=" + username,
                        DATASOURCE_PREFIX + ".password=" + password,
                        LIQUIBASE_PREFIX + ".url=" + jdbcUrl,
                        LIQUIBASE_PREFIX + ".user=" + username,
                        LIQUIBASE_PREFIX + ".password=" + password)
                .applyTo(applicationContext);
    }
}
