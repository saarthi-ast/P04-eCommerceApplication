package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Implements the Error controller related to any errors handled by the Vehicles API
 */
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleExceptions(
            RuntimeException ex, WebRequest request) {
        LOG.error("EXCEPTION - " + ex.getMessage() + " \n Caused by: " + ex.getCause());
        String bodyOfResponse = "EXCEPTION - " + ex.getMessage() + " \n Caused by: " + ex.getCause();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}

