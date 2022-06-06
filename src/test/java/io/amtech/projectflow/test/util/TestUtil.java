package io.amtech.projectflow.test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtil {
    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json()
            .modules(new JavaTimeModule())
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    public static MockHttpServletRequestBuilder get(final String url) {
        return MockMvcRequestBuilders.get(url)
                .characterEncoding(StandardCharsets.UTF_8);
    }

    public static MockHttpServletRequestBuilder post(final String url) {
        return MockMvcRequestBuilders.post(url)
                .characterEncoding(StandardCharsets.UTF_8);
    }

    public static MockHttpServletRequestBuilder delete(final String url) {
        return MockMvcRequestBuilders.delete(url)
                .characterEncoding(StandardCharsets.UTF_8);
    }

    public static MockHttpServletRequestBuilder put(final String url) {
        return MockMvcRequestBuilders.put(url)
                .characterEncoding(StandardCharsets.UTF_8);
    }

    public static MockHttpServletRequestBuilder patch(final String url) {
        return MockMvcRequestBuilders.patch(url)
                .characterEncoding(StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static String readContentFromClassPathResource(final String path) {
        return new String(readAllBytesFromClassPathResource(path));
    }

    @SneakyThrows
    public static byte[] readAllBytesFromClassPathResource(final String path) {
        return Files.readAllBytes(Paths.get(new ClassPathResource(path).getURI()));
    }

    @SneakyThrows
    public static <K, V> Map<K, V> convertJsonToMap(final String json) {
        return OBJECT_MAPPER.readValue(json, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public static <T> T convertJsonToClass(final String json, final Class<T> clazz) {
        return OBJECT_MAPPER.readValue(json, clazz);
    }
}
