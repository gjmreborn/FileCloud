package com.gjm.file_cloud.controller;

import com.gjm.file_cloud.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FilePagingController {
    private final FileService fileService;

    @GetMapping("/files/names/paged/{pageNumber}")
    public List<String> getFileNamesPaged(@PathVariable("pageNumber") int pageNumber) {
        return fileService.getFileNamesPaged(pageNumber);
    }

    @GetMapping("/files/paging")
    public int getPagesCount() {
        return fileService.getPagesCount();
    }
}
