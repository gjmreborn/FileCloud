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

@RestController
@RequestMapping("/api")
public class FileController {
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity addFile(@RequestParam("file") MultipartFile file) {
        fileService.addFile(file);

        String savedFilename = file.getOriginalFilename();
        return createResponseEntity(HttpStatus.CREATED, savedFilename);
    }

    @GetMapping("/file")
    public ResponseEntity getFile(@RequestParam("name") String name) {
        File fetchedFile = fileService.getFileByName(name);

        return createResponseEntity(HttpStatus.OK, fetchedFile);
    }

    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestParam("name") String name) {
        fileService.deleteFile(name);

        return createResponseEntity(HttpStatus.OK, name);
    }

    @GetMapping("/files/names")
    public ResponseEntity getFileNames() {
        List<String> fileNames = fileService.getFileNames();

        return createResponseEntity(HttpStatus.OK, fileNames);
    }

    @GetMapping("/files/zip")
    public ResponseEntity getAllFilesInZip() {
        byte[] zipArchiveInBase64 = Base64.getEncoder().encode(fileService.getAllFilesInZip());

        return createResponseEntity(HttpStatus.OK, zipArchiveInBase64);
    }

    static ResponseEntity createResponseEntity(HttpStatus httpStatus, Object body) {
        return ResponseEntity
                .status(httpStatus)
                .body(body);
    }
}
