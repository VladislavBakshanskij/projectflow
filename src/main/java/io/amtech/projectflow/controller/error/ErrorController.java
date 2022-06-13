package io.amtech.projectflow.controller.error;

import io.amtech.projectflow.error.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({DataNotFoundException.class})
    public ResponseEntity<Object> handleDataNotFoundException(final DataNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return createResponse(status, new ErrorResponse()
                .setCode(status.value())
                .setMessage(e.getMessage()));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return createResponse(status, new ErrorResponse()
                .setCode(status.value())
                .setMessage(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException e,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        ErrorResponse error = new ErrorResponse()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage("Validation error");
        for (FieldError er : e.getFieldErrors()) {
            error.getErrors().put(er.getField(), er.getDefaultMessage());
        }
        if (MapUtils.isEmpty(error.getErrors())) {
            for (ObjectError er : e.getAllErrors()) {
                error.getErrors().put(er.getCode(), er.getDefaultMessage());
            }
        }
        return createResponse(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return createResponse(status, new ErrorResponse()
                .setCode(status.value())
                .setMessage(e.getMessage()));
    }

    @ExceptionHandler({FinalStatusException.class})
    public ResponseEntity<Object> handleFinalStatusException(FinalStatusException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return createResponse(status, new ErrorResponse()
                .setCode(status.value())
                .setMessage(e.getMessage()));
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleSpringAuthException(final AuthenticationException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return createResponse(status, new ErrorResponse()
                .setMessage(e.getMessage())
                .setCode(status.value()));
    }

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<Object> handleAuthException(final AuthException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return createResponse(status, new ErrorResponse()
                .setMessage(e.getMessage())
                .setCode(status.value()));
    }

    @ExceptionHandler({ProcessingException.class})
    public ResponseEntity<Object> handleProcessingException(final ProcessingException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return createResponse(status, new ErrorResponse()
                .setMessage(e.getMessage())
                .setCode(status.value()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(final Exception e) {
        log.error("Not found specific handler for error", e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return createResponse(status, new ErrorResponse()
                .setCode(status.value())
                .setMessage(e.getMessage()));
    }

    private ResponseEntity<Object> createResponse(final HttpStatus status, final ErrorResponse response) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name())
                .body(response);
    }
}
