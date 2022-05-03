package io.amtech.projectflow.controller.profile;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.dto.response.token.TokenDto;
import io.amtech.projectflow.service.token.TokenGenerator;
import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.nio.charset.StandardCharsets;

import static io.amtech.projectflow.test.util.TestUtil.convertJsonToClass;
import static io.amtech.projectflow.test.util.TestUtil.readContentFromClassPathResource;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractMvcTest {
    @Autowired
    private TokenGenerator tokenGenerator;

    @Test
    @Sql(scripts = {
            "classpath:db/users/data.sql"
    })
    @SneakyThrows
    void getProfile() {
        final String json = readContentFromClassPathResource("/json/ProfileControllerTest/getProfile/request/positive_case_auth.json");
        final TokenLoginDto dto = convertJsonToClass(json, TokenLoginDto.class);
        final TokenDto token = tokenGenerator.generate(dto);

        final String response = readContentFromClassPathResource("/json/ProfileControllerTest/getProfile/response/positive_case.json");
        authMvc.perform(get("/profile")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, "Bearer " + token.getAccess()))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }
}