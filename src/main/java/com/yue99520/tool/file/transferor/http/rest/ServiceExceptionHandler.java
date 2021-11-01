package com.yue99520.tool.file.transferor.http.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ServiceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ExceptionResponse> throwableExceptionHandler(Throwable throwable) {
        logger.error("", throwable);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.exception = throwable.getClass().getName();
        exceptionResponse.message = throwable.getMessage();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static class ExceptionResponse {
        String exception;
        String message;
    }
}
