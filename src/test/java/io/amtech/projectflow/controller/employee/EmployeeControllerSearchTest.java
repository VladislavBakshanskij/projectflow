package io.amtech.projectflow.controller.employee;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmployeeControllerSearchTest extends AbstractMvcTest {
    private static final String BASE_URL = "/employees/";

    static Stream<Arguments> searchSuccessArgs() {
        return Stream.of(
                Arguments.arguments("",
                                    readJson("searchSuccess/all.json")),
                Arguments.arguments("?limit=3&offset=2&orders=-name",
                                    readJson("searchSuccess/reverse_order_with_limit_and_offset.json")),
                Arguments.arguments("?name=О",
                                    readJson("searchSuccess/filter_by_name.json")),
                Arguments.arguments("?email=safe",
                                    readJson("searchSuccess/email_contain_safe.json")),
                Arguments.arguments("?phone=9&limit=50",
                                    readJson("searchSuccess/phone_contain_9.json")),
                Arguments.arguments("?position=director&limit=1",
                                    readJson("searchSuccess/position_director.json")),
                Arguments.arguments("?fired=true",
                                    readJson("searchSuccess/fired_employees.json"))
        );
    }

    static Stream<Arguments> searchFailArgs() {
        return Stream.of(Arguments.arguments("?position=MEGA",
                                             readJson("searchFail/invalid_position_response.json"),
                                             HttpStatus.NOT_FOUND.value()));
    }

    private static String readJson(final String path, final Object... args) {
        final String content = TestUtil.readContentFromClassPathResource("json/EmployeeControllerSearchTest/" + path);
        return String.format(content, args);
    }

    @ParameterizedTest
    @MethodSource("searchSuccessArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/employee/search/data.sql"
    })
    void searchSuccess(final String url, final String response) {
        // setup
        mvc.perform(TestUtil.get(BASE_URL + url))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("searchFailArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/employee/search/data.sql"
    })
    void searchFail(final String url, final String response, int status) {
        // setup
        mvc.perform(TestUtil.get(BASE_URL + url))
                .andExpect(status().is(status))
                .andExpect(content().json(response, true));
    }
}
