package io.amtech.projectflow.config;

import io.amtech.projectflow.config.properties.SwaggerProperties;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfiguration {
    private final SwaggerProperties swaggerProperties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .enable(swaggerProperties.getEnabled())
                .protocols(swaggerProperties.getProtocols())
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("io.amtech.projectflow.controller"))
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(auth())
                .operationSelector(this::notExistsInAllowedUrls)
                .build();
    }

    private boolean notExistsInAllowedUrls(OperationContext operationContext) {
        return swaggerProperties.getNotSecuredUrls()
                .stream()
                .filter(url -> operationContext.requestMappingPattern().contains(url))
                .findFirst()
                .isEmpty();
    }

    private List<SecurityReference> auth() {
        return List.of(new SecurityReference("Token Access",
                                             new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")}));
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("Project flow control")
                .version("API 1.0")
                .build();
    }
}
