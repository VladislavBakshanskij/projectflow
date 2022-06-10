package io.amtech.projectflow.test.base;

import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.amtech.projectflow.test.util.TestUtil.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public abstract class AbstractMvcTest extends AbstractIntegrationTest {
    protected MockMvc mvc;
    protected MockMvc authMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mvc = getMockMvcBuilder().build();
        authMvc = getMockMvcBuilder()
                .apply(springSecurity())
                .build();
    }

    protected DefaultMockMvcBuilder getMockMvcBuilder() {
        return MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .addFilter((request, response, chain) -> {
                    request.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    chain.doFilter(request, response);
                });
    }

    @SneakyThrows
    protected String getAccessToken(final String request) {
        final String response = authMvc.perform(post("/auth/login")
                                                        .contentType(APPLICATION_JSON)
                                                        .content(request))
                .andReturn()
                .getResponse()
                .getContentAsString();
        final Map<Object, Object> responseMap = TestUtil.convertJsonToMap(response);
        return responseMap.get("access").toString();
    }
}
