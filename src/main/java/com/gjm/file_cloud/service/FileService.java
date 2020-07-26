package com.gjm.file_cloud.service;

import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.entity.PagingInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void addFile(MultipartFile file);

    File getFileByName(String name);
    List<String> getFileNames();
    byte[] getAllFilesInZip();
    List<String> getFileNamesPaged(int pageNumber);
    PagingInfo getFilePagingInfo();

    void deleteFile(String name);
}
