package com.gjm.file_cloud.controller;

import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class FileController {
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity addFile(@RequestParam("file") MultipartFile file, Locale locale) {
        fileService.addFile(file, locale);

        String savedFilename = file.getOriginalFilename();
        return createResponseEntity(HttpStatus.CREATED, savedFilename);
    }

    @GetMapping("/file")
    public ResponseEntity getFile(@RequestParam("name") String name, Locale locale) {
        File fetchedFile = fileService.getFileByName(name, locale);

        return createResponseEntity(HttpStatus.OK, fetchedFile);
    }

    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestParam("name") String name, Locale locale) {
        fileService.deleteFile(name, locale);

        return createResponseEntity(HttpStatus.OK, name);
    }

    @GetMapping("/files/names")
    public ResponseEntity getFileNames(Locale locale) {
        List<String> fileNames = fileService.getFileNames(locale);

        return createResponseEntity(HttpStatus.OK, fileNames);
    }

    @GetMapping("/files/zip")
    public ResponseEntity getAllFilesInZip(Locale locale) {
        byte[] zipArchiveInBase64 = Base64.getEncoder().encode(fileService.getAllFilesInZip(locale));

        return createResponseEntity(HttpStatus.OK, zipArchiveInBase64);
    }

    static ResponseEntity createResponseEntity(HttpStatus httpStatus, Object body) {
        return ResponseEntity
                .status(httpStatus)
                .body(body);
    }
}
