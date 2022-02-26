package io.amtech.projectflow.controller.project;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;

import java.util.Map;

import static io.amtech.projectflow.test.util.TestUtil.post;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public abstract class AbstractProjectMvcTest extends AbstractMvcTest {
    @SneakyThrows
    protected String getAccessToken() {
        final String response = authMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"user\", \"password\": \"user\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Map<Object, Object> responseMap = TestUtil.convertJsonToMap(response);
        return responseMap.get("access").toString();
    }
}
