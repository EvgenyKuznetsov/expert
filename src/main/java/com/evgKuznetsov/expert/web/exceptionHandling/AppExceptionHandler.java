package com.evgKuznetsov.expert.web.exceptionHandling;

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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.warn("{} is incorrect", request.getDescription(false));

        ProblemInfo info = new ProblemInfo(HttpStatus.BAD_REQUEST);
        for (ConstraintViolation<?> vio : ex.getConstraintViolations()) {
            info.addDetail(vio.getPropertyPath() + " : " + vio.getMessage());
        }
        return new ResponseEntity<>(info, info.getHttpStatus());
    }

    @ExceptionHandler({NoSuchElementException.class})
    protected ResponseEntity<Object> handleNoSuchElement(NoSuchElementException ex, WebRequest request) {

        ProblemInfo info = new ProblemInfo(HttpStatus.NOT_FOUND);

        Map<String, String[]> param = request.getParameterMap();
        if (param.isEmpty()) {
            String path = request.getDescription(false);
            log.warn("nothing here for the path: {}", path);
            info.addDetail("path: " + path);
        } else {
            log.warn("entity not found for query parameters: {}", param);
            StringBuilder builder = new StringBuilder();
            param.forEach((key, value) -> {
                builder.append(key).append(": ");
                for (String val : value) {
                    builder.append(val);
                }
                info.addDetail(builder.toString());
            });
        }
        return new ResponseEntity<>(info, info.getHttpStatus());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.warn(ex.getMessage());
        ProblemInfo info = new ProblemInfo(HttpStatus.UNPROCESSABLE_ENTITY);
        ex.getFieldErrors().forEach(fe -> info.addDetail(fe.getField() + " : " + fe.getDefaultMessage()));

        return new ResponseEntity<>(info, info.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.warn("{} : doesn't have a handler to handle it", ex.getCause().toString());
        ProblemInfo info = new ProblemInfo(HttpStatus.INTERNAL_SERVER_ERROR, List.of("There are not a handler"));
        return new ResponseEntity<>(info, info.getHttpStatus());
    }
}
