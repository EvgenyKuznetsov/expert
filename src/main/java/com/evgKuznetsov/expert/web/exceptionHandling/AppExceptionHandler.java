package com.evgKuznetsov.expert.web.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
@AllArgsConstructor
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private final ResponseBodyCreator responseBodyCreator;

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest wr) {
        ResponseBody responseBody = responseBodyCreator.createResponseBody(ex);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ResponseBody responseBody = responseBodyCreator.createResponseBody(ex);
        return new ResponseEntity<Object>(responseBody, responseBody.getHttpStatus());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ResponseBody responseBody = responseBodyCreator.createResponseBody(ex);
        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }
}
