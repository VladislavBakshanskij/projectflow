package io.amtech.projectflow.test.base;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AbstractMvcTest extends AbstractIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    protected MockMvc mvc;
    protected MockMvc authMvc;

    @BeforeEach
    void setUp() {
        DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .addFilter((request, response, chain) -> {
                    request.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    chain.doFilter(request, response);
                });
        mvc = mockMvcBuilder.build();
        authMvc = mockMvcBuilder
                .apply(springSecurity())
                .build();
    }
}
