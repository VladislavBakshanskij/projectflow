package io.amtech.projectflow.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public class RabbitMQInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        final RabbitMQContainer rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.8-management-alpine")) // :3-management
                .withExposedPorts(5672, 15672)
                .withUser(USERNAME, PASSWORD);
        rabbit.start();

        TestPropertyValues.of("app.rabbit-mq.host=" + rabbit.getHost(),
                              "app.rabbit-mq.port=" + rabbit.getMappedPort(5672),
                              "app.rabbit-mq.username=" + USERNAME,
                              "app.rabbit-mq.password=" + PASSWORD)
                .applyTo(applicationContext);
    }
}
