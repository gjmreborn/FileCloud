package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.FileDao;
import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.entity.User;
import com.gjm.file_cloud.exceptions.http.FileDoesntExistException;
import com.gjm.file_cloud.exceptions.http.FileDuplicationException;
import com.gjm.file_cloud.exceptions.http.NoFilesException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceDatabaseImpl implements FileService {
    private final FileDao fileDao;
    private final UserDao userDao;
    private final AuthenticationService authenticationService;

    @Override
    public void addFile(@Valid File file) {
        String username = authenticationService.getUsernameOfLoggedInUser();

        try {
            getFileByName(file.getName());
            throw new FileDuplicationException();
        } catch(FileDoesntExistException exc) {
            fileDao.save(file);

            User currentUser = userDao.findByUsername(username).orElseThrow();

            // update user's files
            List<File> userFiles = currentUser.getFiles();
            userFiles.add(file);
            currentUser.setFiles(userFiles);

            // update user to new files (old files with new added)
            userDao.save(currentUser);      // update operation (ID stays the same)
        }
    }

    @Override
    public Page<File> getFiles(int pageNumber) {
        return fileDao.findFilesByOwnerName(
                authenticationService.getUsernameOfLoggedInUser(),
                PageRequest.of(pageNumber - 1, 5)
        );
    }

    @Override
    public void deleteFile(String name) {
        String username = authenticationService.getUsernameOfLoggedInUser();

        File fileToDelete = getFileByName(name);
        User currentUser = userDao.findByUsername(username).orElseThrow();

        List<File> userFiles = currentUser.getFiles();
        userFiles.remove(fileToDelete);
        currentUser.setFiles(userFiles);

        // update user to new files (old files without already deleted one)
        userDao.save(currentUser);          // update operation (ID stays the same)

        fileDao.delete(fileToDelete);
    }

    @Override
    public File getFileByName(String name) {
        String username = authenticationService.getUsernameOfLoggedInUser();

        List<File> foundFile = userDao.findByUsername(username).orElseThrow().getFiles()
                .stream()
                .parallel()
                .filter(file -> file.getName().equals(name))
                .collect(Collectors.toList());

        if(foundFile.isEmpty()) {
//            throw new FileDoesntExistException("File " + name + " doesn't exist!");
            throw new FileDoesntExistException();
        } else {
            return foundFile.get(0);
        }
    }

    @Override
    public List<String> getFileNames() {
        String username = authenticationService.getUsernameOfLoggedInUser();

        List<String> names = userDao.findByUsername(username).orElseThrow().getFiles()
                .stream()
                .parallel()
                .map(File::getName)
                .collect(Collectors.toList());

        if(names.isEmpty()) {
//            throw new NoFilesException("No files stored in FileCloud!");
            throw new NoFilesException();
        } else {
            return names;
        }
    }

    @Override
    public byte[] getZippedFiles() {
        String username = authenticationService.getUsernameOfLoggedInUser();

        List<File> files = userDao.findByUsername(username).orElseThrow().getFiles();

        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(bos)) {
            zos.setComment("Compressed all files!");

            for(File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                zos.write(file.getBytes());
            }

            zos.finish();
            byte[] resultByteArray = bos.toByteArray();

            if(resultByteArray.length == 0) {
//                throw new NoFilesException("No files stored in FileCloud!");
                throw new NoFilesException();
            } else {
                return resultByteArray;
            }
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getFileNamesPaged(int pageNumber) {
        return fileDao.findFilesByOwnerName(
                authenticationService.getUsernameOfLoggedInUser(),
                PageRequest.of(pageNumber - 1, 5)
        )
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public int getPagesCount() {
        return (int) Math.ceil(userDao.findByUsername(authenticationService.getUsernameOfLoggedInUser()).orElseThrow()
                .getFiles()
                .size() / 5.0);
    }
}
