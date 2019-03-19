package com.gjm.file_cloud.exceptions.file_cloud_runtime_exception;

import com.gjm.file_cloud.exceptions.FileCloudRuntimeException;

public class FileDoesntExistException extends FileCloudRuntimeException {
    public FileDoesntExistException(String desc) {
        super(desc);
    }
}
