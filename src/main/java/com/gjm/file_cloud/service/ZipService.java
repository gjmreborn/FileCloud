package com.gjm.file_cloud.service;

import com.gjm.file_cloud.entity.File;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {
    public byte[] createArchive(List<File> files) {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(bos)) {
            zos.setComment("Compressed all files!");

            for(File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                zos.write(file.getBytes());
            }
            zos.finish();

            return bos.toByteArray();
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        return null;
    }
}
