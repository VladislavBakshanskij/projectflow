package io.amtech.projectflow.test.base;

import io.amtech.projectflow.config.TestRabbitAdminConfig;
import io.amtech.projectflow.config.TestRedisConfiguration;
import io.amtech.projectflow.config.TestTransactionalUtilConfig;
import io.amtech.projectflow.initializer.PostgreSqlInitializer;
import io.amtech.projectflow.initializer.RabbitMQInitializer;
import io.amtech.projectflow.test.util.TransactionalUtil;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:db/clean.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ContextConfiguration(
        classes = {
                TestTransactionalUtilConfig.class,
                TestRedisConfiguration.class,
                TestRabbitAdminConfig.class
        },
        initializers = {
                PostgreSqlInitializer.class,
                RabbitMQInitializer.class
        }
)
public abstract class AbstractIntegrationTest {
    @Autowired
    protected TransactionalUtil transactionalUtil;

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    protected RabbitAdmin rabbitAdmin;

    protected void assertThatQueueInfo(final String queueInfo, final Consumer<QueueInformation> queueInformationConsumer) {
        assertThat(rabbitAdmin.getQueueInfo(queueInfo))
                .isNotNull()
                .satisfies(queueInformationConsumer);
    }
}
