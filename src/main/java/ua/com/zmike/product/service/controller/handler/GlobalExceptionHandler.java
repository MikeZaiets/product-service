package ua.com.zmike.product.service.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ua.com.zmike.product.service.exception.TargetNotFoundException;
import ua.com.zmike.product.service.exception.dto.ExceptionDto;

import javax.validation.ConstraintViolationException;

@Slf4j(topic = "ExceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String NOT_FOUND_REASON = "Not found";
    private static final String REQUEST_CONTENT_REASON = "Request content";
    private static final String UNEXPECTED_ERROR_REASON = "Unexpected error";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TargetNotFoundException.class)
    public ExceptionDto storageException(TargetNotFoundException ex) {
        log.warn("Target not found exception, {}", ex.getMessage());
        return buildExceptionDto(NOT_FOUND_REASON, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ExceptionDto parseExceptionsHandler(Exception ex) {
        log.error("Incorrect incoming request {}", ex.getLocalizedMessage(), ex);
        return buildExceptionDto(REQUEST_CONTENT_REASON, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionDto genericHandler(Exception ex) {
        log.error("Unhandled exception {}, message {}", ex.getClass().getCanonicalName(), ex.getLocalizedMessage(), ex);
        return buildExceptionDto(UNEXPECTED_ERROR_REASON, ex.getMessage());
    }

    private ExceptionDto buildExceptionDto(String reason, String message) {
        return new ExceptionDto(reason, message);
    }
}
