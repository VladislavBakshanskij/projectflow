package io.amtech.projectflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.amtech.projectflow.config.properties.RabbitMQProperties;
import io.amtech.projectflow.error.ConfigurationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static io.amtech.projectflow.constants.RabbitMQConstants.*;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {
    private final RabbitMQProperties rabbitMQProperties;
    private final ObjectMapper objectMapper;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(configureConnectionFactory(new com.rabbitmq.client.ConnectionFactory()));
    }

    private com.rabbitmq.client.ConnectionFactory configureConnectionFactory(com.rabbitmq.client.ConnectionFactory connectionFactory) {
        try {
            log.debug("configure connection factory with props :: {}", rabbitMQProperties);

            if (isNotBlank(rabbitMQProperties.getUri())) {
                connectionFactory.setUri(rabbitMQProperties.getUri());
            } else if (nonNull(rabbitMQProperties.getPort()) &&
                    isNoneBlank(rabbitMQProperties.getHost(), rabbitMQProperties.getUsername(), rabbitMQProperties.getPassword())) {
                connectionFactory.setHost(rabbitMQProperties.getHost());
                connectionFactory.setPort(rabbitMQProperties.getPort());
                connectionFactory.setUsername(rabbitMQProperties.getUsername());
                connectionFactory.setPassword(rabbitMQProperties.getUsername());
            } else {
                throw new ConfigurationException("Не заданы параметры подключения для rabbitmq");
            }

            return connectionFactory;
        } catch (URISyntaxException e) {
            log.error("Invalid uri for configure connection factory :: {}", e.getMessage());
            throw new ConfigurationException("Некорректный адрес подключения для rabbtimq", e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("Not found algorihm for ssl connection :: {}", e.getMessage());
            throw new ConfigurationException("Не удалось создать защищенное подключение", e);
        }
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public DirectExchange notificationDirectExchange() {
        return new DirectExchange(NOTIFICATION_PROJECT_EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(notificationQueue()).to(notificationDirectExchange()).with(NOTIFY_PROJECT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper); // by default Jackson2JsonMessageConverter create new ObjectMapper
    }
}
