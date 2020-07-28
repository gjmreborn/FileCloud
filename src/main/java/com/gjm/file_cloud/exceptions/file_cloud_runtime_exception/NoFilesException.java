package com.gjm.file_cloud.exceptions.file_cloud_runtime_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoFilesException extends RuntimeException {
}
