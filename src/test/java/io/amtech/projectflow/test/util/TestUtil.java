package io.amtech.projectflow.test.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtil {
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

    @SneakyThrows
    public static String readContentFromClassPathResource(final String path) {
        return new String(readAllBytesFromClassPathResource(path));
    }

    @SneakyThrows
    public static byte[] readAllBytesFromClassPathResource(final String path) {
        return Files.readAllBytes(Paths.get(new ClassPathResource(path).getURI()));
    }
}
