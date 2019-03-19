package com.gjm.file_cloud.advices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionsAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalPathExceptionHandler(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }
}
