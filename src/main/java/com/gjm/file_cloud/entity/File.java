package com.gjm.file_cloud.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "files")
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "file_id")
    private long id;

    @NotNull(message = "You must provide file name")
    @NotEmpty(message = "File name can't be empty")
    private String name;

    @NotNull(message = "You must provide file type")
    @NotEmpty(message = "File type can't be empty")
    private String type;

    @Lob
    @NotNull(message = "You must provide file content")
    @NotEmpty(message = "File content can't be empty")
    private byte[] bytes;

    public File() {
    }

    public File(String name, String type, byte[] bytes) {
        this.name = name;
        this.type = type;
        this.bytes = bytes;
    }
}
