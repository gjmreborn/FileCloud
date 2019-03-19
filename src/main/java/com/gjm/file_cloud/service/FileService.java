package com.gjm.file_cloud.service;

import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.entity.PagingInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

public interface FileService {
    void addFile(MultipartFile file, Locale locale);

    File getFileByName(String name, Locale locale);
    List<String> getFileNames(Locale locale);
    byte[] getAllFilesInZip(Locale locale);
    List<String> getFileNamesPaged(int pageNumber, Locale locale);
    PagingInfo getFilePagingInfo(Locale locale);

    void deleteFile(String name, Locale locale);
}
