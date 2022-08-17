package com.evgKuznetsov.expert.exception.hanling;

import com.evgKuznetsov.expert.exception.IllegalRequestDataException;
import com.evgKuznetsov.expert.exception.ProblemInfo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
@Slf4j
@AllArgsConstructor
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.warn("{} is incorrect", request.getDescription(false));

        ProblemInfo info = new ProblemInfo(HttpStatus.BAD_REQUEST);
        String parameterName = "";
        for (ConstraintViolation<?> vio : ex.getConstraintViolations()) {
            Path path = vio.getPropertyPath();
            for (Path.Node node : path) {
                if (node.getKind().equals(ElementKind.PARAMETER)) {
                    parameterName = node.getName();
                }
            }
            info.addDetail(parameterName + " : " + vio.getMessage());
        }
        return new ResponseEntity<>(info, info.getHttpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false);
        String message = messageSource.getMessage("validation.data.notfound", null, request.getLocale());
        String detail = String.format("%s : %s", path, message);

        log.warn("EntityNotFoundException: {}", path);

        ProblemInfo info = new ProblemInfo(HttpStatus.NOT_FOUND, Set.of(detail));
        return new ResponseEntity<>(info, info.getHttpStatus());
    }

    @ExceptionHandler(IllegalRequestDataException.class)
    protected ResponseEntity<Object> handleIllegalRequestData(IllegalRequestDataException ex, WebRequest request) {
        String message = ex.getMessage();
        log.warn("IllegalRequestDataException, with a message: {}", message);
        if (Strings.isBlank(message)) {
            message = messageSource.getMessage("default", null, request.getLocale());
        }
        ProblemInfo info = new ProblemInfo(ex.getStatus(), Set.of(message));
        return new ResponseEntity<>(info, info.getHttpStatus());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        ProblemInfo info = new ProblemInfo(HttpStatus.UNPROCESSABLE_ENTITY);
        BindingResult bindingResult = ex.getBindingResult();
        List<String> cause = new ArrayList<>();

        if (bindingResult.hasFieldErrors()) {
            ex.getFieldErrors().forEach(fe -> {
                cause.add(fe.getField());
                info.addDetail(fe.getField() + " : " + fe.getDefaultMessage());
            });
        }

        log.warn("For {} there is unprocessable {}",
                request.getDescription(false),
                cause);

        return new ResponseEntity<>(info, info.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.warn("{} : doesn't have a handler to handle it", ex.getCause().toString());
        final String message = ExceptionUtils.getRootCauseMessage(ex);
        ProblemInfo info = new ProblemInfo(HttpStatus.INTERNAL_SERVER_ERROR, Set.of(message));
        return new ResponseEntity<>(info, info.getHttpStatus());
    }
}
