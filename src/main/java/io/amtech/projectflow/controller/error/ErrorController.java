package io.amtech.projectflow.controller.error;

import io.amtech.projectflow.error.AuthException;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.error.ProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({DataNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(final DataNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ErrorResponse()
                .setCode(status.value())
                .setMessage(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage("Validation error");
        for (FieldError er : e.getFieldErrors()) {
            error.getErrors().put(er.getField(), er.getDefaultMessage());
        }
        for (ObjectError er : e.getAllErrors()) {
            error.getErrors().put(er.getObjectName(), er.getDefaultMessage());
        }
        return error;
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleSpringAuthException(final AuthenticationException e) {
        return new ErrorResponse()
                .setMessage(e.getMessage())
                .setCode(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({AuthException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthException(final AuthException e) {
        return new ErrorResponse()
                .setMessage(e.getMessage())
                .setCode(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({ProcessingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleProcessingException(final ProcessingException e) {
        return new ErrorResponse()
                .setMessage(e.getMessage())
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        return new ErrorResponse()
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(e.getMessage());
    }
}
