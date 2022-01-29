package io.amtech.projectflow.controller.error;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ErrorResponse {
    private int code;
    private String message;
    private Map<String, String> errors = new HashMap<>();
}
