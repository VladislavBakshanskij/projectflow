package io.amtech.projectflow.controller.user;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractMvcTest {
    private static final String BASE_URL = "/users";

    @Test
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/auth/users.sql"
    })
    void getInfoSuccess() {
        final String token = getAccessToken("{\"username\": \"user\", \"password\": \"user\"}");
        final String response = TestUtil.readContentFromClassPathResource("/json/UserControllerTest/getInfoSuccess/response/positive_case.json");
        authMvc.perform(get(BASE_URL + "/info")
                                .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }
}