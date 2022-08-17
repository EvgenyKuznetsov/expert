package com.evgKuznetsov.expert.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class IllegalRequestDataException extends AppException {

    public IllegalRequestDataException(String message) {
        super(UNPROCESSABLE_ENTITY, message, ErrorAttributeOptions.of(MESSAGE));
    }
}