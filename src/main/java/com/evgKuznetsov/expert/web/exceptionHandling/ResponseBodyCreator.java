package com.evgKuznetsov.expert.web.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Component
public class ResponseBodyCreator {

    public ResponseBody createResponseBody(Exception ex) {
        if (ex instanceof ConstraintViolationException) {
            return validationException((ConstraintViolationException) ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            return methodException((MethodArgumentNotValidException) ex);
        } else if (ex instanceof BindException) {
            return globalExceptionHandler((BindException) ex);
        } else {
            return new ResponseBody(HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    List.of("There are not a handler"));
        }
    }

    private ResponseBody validationException(ConstraintViolationException ex) {

        ResponseBody responseBody = new ResponseBody();
        responseBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        responseBody.setTitle("INCORRECT REQUEST PARAMETERS");

        Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
        for (ConstraintViolation<?> cv : set) {
            String detail = cv.getPropertyPath() + " : " + cv.getMessage();
            responseBody.addDetail(detail);
        }

        return responseBody;
    }

    private ResponseBody methodException(MethodArgumentNotValidException ex) {

        ResponseBody rb = new ResponseBody();
        rb.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        rb.setTitle("INCORRECT REQUEST BODY");
        ex.getFieldErrors().stream()
                .forEach(fe -> rb.addDetail(fe.getField() + " : " + fe.getDefaultMessage()));

        return rb;
    }

    private ResponseBody globalExceptionHandler(BindException ex) {
        ResponseBody rb = new ResponseBody();
        rb.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        rb.setTitle("ERROR OCCURRED");
        rb.addDetail(ex.getLocalizedMessage());
        return rb;
    }
}
