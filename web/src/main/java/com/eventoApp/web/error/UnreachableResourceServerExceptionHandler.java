package com.eventoApp.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnreachableResourceServerExceptionHandler {

    //Handler para lidar com UnreachableResourceServerException que for lançada
    @ExceptionHandler
    public ResponseEntity<UnreachableResourceServerErrorResponse> handleException(UnreachableResourceServerException exc) {

        UnreachableResourceServerErrorResponse error = new UnreachableResourceServerErrorResponse(HttpStatus.FORBIDDEN.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    //Handler para lidar com quer exception lançada
    @ExceptionHandler
    public ResponseEntity<UnreachableResourceServerErrorResponse> handleException(Exception exc) {

        UnreachableResourceServerErrorResponse error = new UnreachableResourceServerErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
