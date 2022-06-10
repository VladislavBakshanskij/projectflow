package io.amtech.projectflow.controller.project;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;

public abstract class AbstractProjectMvcTest extends AbstractMvcTest {
    @SneakyThrows
    protected String getDirectorAccessToken() {
        return getAccessToken("{\"username\": \"user\", \"password\": \"user\"}");
    }

    @SneakyThrows
    protected String getProjectLeadAccessToken() {
        return getAccessToken("{\"username\": \"vlad\", \"password\": \"user\"}");
    }
}
