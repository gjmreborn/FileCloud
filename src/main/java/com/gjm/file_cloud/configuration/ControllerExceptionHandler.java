package com.gjm.file_cloud.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException exc) {
        return new ResponseEntity<>(
                stringifyValidationErrors(exc.getBindingResult().getFieldErrors()),
                HttpStatus.BAD_REQUEST
        );
    }

    private String stringifyValidationErrors(List<FieldError> fieldErrors) {
        StringBuilder errorString = new StringBuilder();

        for(FieldError error : fieldErrors) {
            errorString.append(error.getField())
                    .append(": ").append(error.getDefaultMessage())
                    .append("\n");
        }

        return errorString.toString();
    }
}
