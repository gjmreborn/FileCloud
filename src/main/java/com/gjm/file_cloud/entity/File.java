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

    @NotNull(message = "file_cloud.messages.validation.name.not_null")
    @NotEmpty(message = "file_cloud.messages.validation.name.not_empty")
    private String name;

    @NotNull(message = "file_cloud.messages.validation.type.not_null")
    @NotEmpty(message = "file_cloud.messages.validation.type.not_empty")
    private String type;

    @Lob
    @NotNull(message = "file_cloud.messages.validation.bytes.not_null")
    @NotEmpty(message = "file_cloud.messages.validation.bytes.not_empty")
    private byte[] bytes;

    public File() {
    }

    public File(String name, String type, byte[] bytes) {
        this.name = name;
        this.type = type;
        this.bytes = bytes;
    }
}
