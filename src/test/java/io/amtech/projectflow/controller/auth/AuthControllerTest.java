package io.amtech.projectflow.controller.auth;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;
import java.util.UUID;

import static io.amtech.projectflow.test.util.TestUtil.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest extends AbstractMvcTest {
    private static final String BASE_URL = "/auth/";

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/auth/data.sql")
    void loginSuccess() {
        final String request = readJson("loginSuccess/request/positive_case.json");

        final String response = authMvc.perform(post(BASE_URL + "login")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").hasJsonPath())
                .andExpect(jsonPath("$.refresh").hasJsonPath())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final Map<String, UUID> parsedResponse = convertJsonToMap(response);

        authMvc.perform(get("/employees/1d97755d-d424-4128-8b78-ca5cb7b015c9")
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + parsedResponse.get("access")))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/auth/data.sql")
    void loginFailed() {
        final String request = readJson("loginFailed/request/negative_case_password_is_incorrect.json");
        final String response = readJson("loginFailed/response/negative_case_password_is_incorrect.json");

        authMvc.perform(post(BASE_URL + "login")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response, true));
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/auth/data.sql")
    void refreshSuccess() {
        final String requestAccessToken = readJson("refreshSuccess/request/positive_case_login.json");

        final String access = authMvc.perform(post(BASE_URL + "login")
                        .contentType(APPLICATION_JSON)
                        .content(requestAccessToken))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final String token = convertJsonToMap(access)
                .getOrDefault("refresh", EMPTY)
                .toString();

        final String requestRefreshToken = readJson("refreshSuccess/request/positive_case_refresh.json", token);

        authMvc.perform(post(BASE_URL + "refresh")
                        .contentType(APPLICATION_JSON)
                        .content(requestRefreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").isNotEmpty())
                .andExpect(jsonPath("$.refresh").isNotEmpty());
    }

    private static String readJson(final String path, final Object... args) {
        final String content = TestUtil.readContentFromClassPathResource("/json/AuthControllerTest/" + path);
        return String.format(content, args);
    }
}
