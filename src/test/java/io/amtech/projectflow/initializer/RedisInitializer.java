package io.amtech.projectflow.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final int DEFAULT_REDIS_PORT = 6379;

    private static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:alpine"))
            .withExposedPorts(DEFAULT_REDIS_PORT);

    private static final String REDIS_CONFIG_PREFIX = "spring.redis";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        REDIS_CONTAINER.start();

        TestPropertyValues.of(REDIS_CONFIG_PREFIX + ".host=" + REDIS_CONTAINER.getHost(),
                        REDIS_CONFIG_PREFIX + ".port=" + REDIS_CONTAINER.getMappedPort(DEFAULT_REDIS_PORT))
                .applyTo(applicationContext);
    }
}
