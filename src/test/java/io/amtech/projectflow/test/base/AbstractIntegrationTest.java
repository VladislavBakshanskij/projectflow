package io.amtech.projectflow.test.base;

import io.amtech.projectflow.config.TestTransactionalUtilConfig;
import io.amtech.projectflow.initializer.PostgreSqlInitializer;
import io.amtech.projectflow.initializer.RedisInitializer;
import io.amtech.projectflow.test.util.TransactionalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:db/clean.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ContextConfiguration(
        classes = TestTransactionalUtilConfig.class,
        initializers = {
                PostgreSqlInitializer.class,
                RedisInitializer.class
        }
)
public abstract class AbstractIntegrationTest {
    @Autowired
    protected TransactionalUtil transactionalUtil;
}
