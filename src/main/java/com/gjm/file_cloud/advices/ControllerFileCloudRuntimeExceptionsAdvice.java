package com.gjm.file_cloud.advices;

import com.gjm.file_cloud.exceptions.file_cloud_runtime_exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerFileCloudRuntimeExceptionsAdvice {
    @ExceptionHandler(NoFilesException.class)
    public ResponseEntity noFilesExceptionHandler(NoFilesException exception) {
        return exception.toResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(FileDoesntExistException.class)
    public ResponseEntity fileDoesntExistExceptionHandler(FileDoesntExistException exception) {
        return exception.toResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileDuplicationException.class)
    public ResponseEntity fileDuplicationExceptionHandler(FileDuplicationException exception) {
        return exception.toResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity fileValidationExceptionHandler(FileValidationException exception) {
        return exception.toResponseEntity(HttpStatus.NOT_EXTENDED);
    }

    @ExceptionHandler(FilePagingException.class)
    public ResponseEntity filePagingExceptionHandler(FilePagingException exception) {
        return exception.toResponseEntity(HttpStatus.CONFLICT);
    }
}
