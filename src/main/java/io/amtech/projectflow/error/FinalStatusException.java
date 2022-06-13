package io.amtech.projectflow.error;

public class FinalStatusException extends RuntimeException {
    public FinalStatusException(final String message) {
        super(message);
    }
}
