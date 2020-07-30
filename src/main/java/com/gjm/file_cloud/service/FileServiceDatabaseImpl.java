package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.FileDao;
import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.entity.User;
import com.gjm.file_cloud.exceptions.http.FileAlreadyExistsException;
import com.gjm.file_cloud.exceptions.http.FileDoesntExistException;
import com.gjm.file_cloud.exceptions.http.NoFilesException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceDatabaseImpl implements FileService {
    public static final int PAGE_SIZE = 5;

    private final FileDao fileDao;
    private final UserDao userDao;
    private final AuthenticationService authenticationService;
    private final ZipService zipService;

    @Override
    public void addFile(@Valid File file) {
        String username = authenticationService.getUsernameOfLoggedInUser();
        User currentUser = userDao.findByUsername(username)
                .orElseThrow();

        if(fileExists(username, file.getName())) {
            throw new FileAlreadyExistsException();
        }

        addFileToUser(file, currentUser);
        userDao.save(currentUser);
    }

    @Override
    public Page<File> getFiles(int pageNumber) {
        return fileDao.findFilesByOwnerName(
                authenticationService.getUsernameOfLoggedInUser(),
                PageRequest.of(pageNumber - 1, PAGE_SIZE)
        );
    }

    @Override
    public void deleteFile(String name) {
        String username = authenticationService.getUsernameOfLoggedInUser();
        User currentUser = userDao.findByUsername(username)
                .orElseThrow();
        File fileToDelete = getFileByName(name);

        removeFileFromUser(fileToDelete, currentUser);
        userDao.save(currentUser);
    }

    @Override
    public File getFileByName(String name) {
        String username = authenticationService.getUsernameOfLoggedInUser();

        List<File> content = fileDao.findFilesByOwnerName(username, null)
                .getContent()
                .stream()
                .filter(file -> file.getName().equals(name))
                .collect(Collectors.toList());
        if(content.isEmpty()) {
            throw new FileDoesntExistException();
        } else {
            return content.get(0);
        }
    }

    @Override
    public List<String> getFileNames() {
        String username = authenticationService.getUsernameOfLoggedInUser();

        List<String> content = fileDao.findFilesByOwnerName(username, null)
                .getContent()
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());
        if(content.isEmpty()) {
            throw new NoFilesException();
        } else {
            return content;
        }
    }

    @Override
    public byte[] getZippedFiles() {
        String username = authenticationService.getUsernameOfLoggedInUser();
        List<File> files = fileDao.findFilesByOwnerName(username, null)
                .getContent();

        if(files.isEmpty()) {
            throw new NoFilesException();
        }
        return zipService.createArchive(files);
    }

    @Override
    public List<String> getFileNamesPaged(int pageNumber) {
        return getFiles(pageNumber)
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public int getPagesCount() {
        String username = authenticationService.getUsernameOfLoggedInUser();

        return fileDao.findFilesByOwnerName(username, PageRequest.of(0, PAGE_SIZE))
                .getTotalPages();
    }

    private boolean fileExists(String username, String fileName) {
        return fileDao.findFilesByOwnerName(username, null)
                .stream()
                .anyMatch(file -> file.getName().equals(fileName));
    }

    private void addFileToUser(File file, User user) {
        file.setOwner(user);

        List<File> userFiles = user.getFiles();
        userFiles.add(file);
        user.setFiles(userFiles);
    }

    private void removeFileFromUser(File file, User user) {
        List<File> userFiles = user.getFiles();
        userFiles.remove(file);
        user.setFiles(userFiles);
    }
}
