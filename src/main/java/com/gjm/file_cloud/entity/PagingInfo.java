package com.gjm.file_cloud.entity;

import lombok.Data;

@Data
public class PagingInfo {
    private int numberOfPages;
    private int recordsPerPage;
    private int totalRecords;
}
