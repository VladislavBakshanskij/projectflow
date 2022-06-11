package io.amtech.projectflow.config.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@Accessors(chain = true)
@ConfigurationProperties(prefix = "app.rabbit-mq")
public class RabbitMQProperties {
    private String uri;
    private String host;
    private Integer port;
    private String username;
    private String password;
}
