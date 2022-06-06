package io.amtech.projectflow.controller.direction;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DirectionControllerSearchTest extends AbstractMvcTest {
    private static final String BASE_URL = "/directions/";

    static Stream<Arguments> searchSuccessArgs() {
        return Stream.of(
                Arguments.arguments("",
                                    readJson("searchSuccess/all_directions_response.json")),
                Arguments.arguments("?limit=3&offset=1&orders=-name",
                                    readJson("searchSuccess/reverse_order_with_limit_and_offset_response.json")),
                Arguments.arguments("?name=W",
                                    readJson("searchSuccess/filter_by_name_response.json"))
        );
    }

    private static String readJson(final String path, final Object... args) {
        final String content = TestUtil.readContentFromClassPathResource("/json/DirectionControllerSearchTest/" + path);
        return String.format(content, args);
    }

    @ParameterizedTest
    @MethodSource("searchSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/search/data.sql")
    void searchSuccess(final String url, final String response) {
        mvc.perform(TestUtil.get(BASE_URL + url))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }
}
