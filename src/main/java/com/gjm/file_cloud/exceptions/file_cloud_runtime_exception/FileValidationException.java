package com.gjm.file_cloud.exceptions.file_cloud_runtime_exception;

import com.gjm.file_cloud.exceptions.FileCloudRuntimeException;

public class FileValidationException extends FileCloudRuntimeException {
    public FileValidationException(String desc) {
        super(desc);
    }
}
