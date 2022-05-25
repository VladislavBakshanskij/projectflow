package io.amtech.projectflow.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Validated
@ConfigurationProperties(prefix = "app.swagger")
public class SwaggerProperties {
    @NotNull
    private Boolean enabled;
    @NotEmpty
    private Set<@NotBlank String> protocols = new HashSet<>();
    private Set<@NotBlank String> notSecuredUrls = new HashSet<>();
}
