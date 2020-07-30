package com.gjm.file_cloud.service;

import com.gjm.file_cloud.entity.File;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface FileService {
    void addFile(@Valid File file);

    Page<File> getFiles(int pageNumber);
    List<String> getFileNames();
    long getFileCount();
    List<String> getFileNamesPaged(int pageNumber);
    File getFileByName(String name);
    byte[] getZippedFiles();
    int getPagesCount();

    void deleteFile(String name);
}
