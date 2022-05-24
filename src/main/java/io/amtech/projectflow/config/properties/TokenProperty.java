package io.amtech.projectflow.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "app.redis")
public class TokenProperty {
    @NotNull
    private Long accessTokenTimeToLive;
    @NotNull
    private Long refreshTokenTimeToLive;
}
