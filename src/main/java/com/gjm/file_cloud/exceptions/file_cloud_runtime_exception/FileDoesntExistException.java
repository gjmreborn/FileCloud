package com.gjm.file_cloud.exceptions.file_cloud_runtime_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileDoesntExistException extends RuntimeException {
}
