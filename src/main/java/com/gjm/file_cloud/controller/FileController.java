package com.gjm.file_cloud.controller;

import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<String> addFile(@RequestParam("file") MultipartFile file) throws Exception {
        fileService.addFile(new File(file.getOriginalFilename(), file.getContentType(), file.getBytes()));

        return new ResponseEntity<>(file.getOriginalFilename(), HttpStatus.CREATED);
    }

    @GetMapping("/file")
    public File getFile(@RequestParam("name") String name) {
        return fileService.getFileByName(name);
    }

    @DeleteMapping("/file")
    public String deleteFile(@RequestParam("name") String name) {
        fileService.deleteFile(name);

        return name;
    }

    @GetMapping("/files/names")
    public List<String> getFileNames() {
        return fileService.getFileNames();
    }

    @GetMapping("/files/zip")
    public byte[] getAllFilesInZip() {
        return Base64.getEncoder()
                .encode(fileService.getZippedFiles());
    }
}
