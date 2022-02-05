package io.amtech.projectflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.redis")
@Data
public class TokenProperty {
    private long accessTokenTimeToLive;
    private long refreshTokenTimeToLive;
}
