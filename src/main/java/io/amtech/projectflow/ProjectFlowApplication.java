package io.amtech.projectflow;

import io.amtech.projectflow.config.properties.SwaggerProperties;
import io.amtech.projectflow.config.properties.TokenProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {
        R2dbcAutoConfiguration.class
})
@EnableConfigurationProperties({
        TokenProperty.class,
        SwaggerProperties.class
})
public class ProjectFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectFlowApplication.class, args);
    }

}
