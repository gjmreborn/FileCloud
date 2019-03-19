package com.gjm.file_cloud.exceptions.file_cloud_runtime_exception;

import com.gjm.file_cloud.exceptions.FileCloudRuntimeException;

public class NoFilesException extends FileCloudRuntimeException {
    public NoFilesException(String desc) {
        super(desc);
    }
}
