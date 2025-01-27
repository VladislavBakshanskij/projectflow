package io.amtech.projectflow.config;

import io.amtech.projectflow.test.util.TransactionalUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("test")
@TestConfiguration
public class TestTransactionalUtilConfig {
    @Bean
    public TransactionalUtil transactionalUtil() {
        return new TransactionalUtil();
    }
}
