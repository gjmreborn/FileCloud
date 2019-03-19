package com.gjm.file_cloud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

abstract public class FileCloudRuntimeException extends RuntimeException {
    public FileCloudRuntimeException(String desc) {
        super(desc);
    }

    public ResponseEntity toResponseEntity(HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(getMessage());
    }
}
