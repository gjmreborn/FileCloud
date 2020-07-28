package com.gjm.file_cloud.service;

import com.gjm.file_cloud.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void addFile(MultipartFile file);

    Page<File> getFiles(int pageNumber);
    List<String> getFileNames();
    List<String> getFileNamesPaged(int pageNumber);
    File getFileByName(String name);
    byte[] getZippedFiles();
    int getPagesCount();

    void deleteFile(String name);
}
