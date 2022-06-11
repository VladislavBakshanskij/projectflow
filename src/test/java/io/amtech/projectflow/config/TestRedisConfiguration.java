package io.amtech.projectflow.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Profile("test")
@TestConfiguration
public class TestRedisConfiguration {
    @Bean(destroyMethod = "stop")
    public GenericContainer<?> redis() {
        final GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:alpine")
                                                                                  .asCompatibleSubstituteFor("redis"))
                .withExposedPorts(6379);
        redisContainer.start();
        return redisContainer;
    }

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory(GenericContainer<?> redis) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redis.getHost());
        configuration.setDatabase(1);
        configuration.setPort(redis.getMappedPort(6379));
        return new LettuceConnectionFactory(configuration);
    }
}
