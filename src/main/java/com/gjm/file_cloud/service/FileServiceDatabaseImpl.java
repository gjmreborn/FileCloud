package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.FileDao;
import com.gjm.file_cloud.dao.UserDao;
import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.entity.PagingInfo;
import com.gjm.file_cloud.entity.User;
import com.gjm.file_cloud.exceptions.file_cloud_runtime_exception.FileDoesntExistException;
import com.gjm.file_cloud.exceptions.file_cloud_runtime_exception.FileDuplicationException;
import com.gjm.file_cloud.exceptions.file_cloud_runtime_exception.FilePagingException;
import com.gjm.file_cloud.exceptions.file_cloud_runtime_exception.NoFilesException;
import com.gjm.file_cloud.validator.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
public class FileServiceDatabaseImpl implements FileService {
    private FileDao fileDao;
    private UserDao userDao;

    private AuthenticationService authenticationService;
    private FileValidator fileValidator;

    @Autowired
    public FileServiceDatabaseImpl(FileDao fileDao, UserDao userDao, AuthenticationService authenticationService, FileValidator fileValidator) {
        this.fileDao = fileDao;
        this.userDao = userDao;
        this.authenticationService = authenticationService;
        this.fileValidator = fileValidator;
    }

    @Override
    public void addFile(MultipartFile file) {
        String username = authenticationService.getUsernameOfLoggedInUser();

        try {
            getFileByName(file.getOriginalFilename());

            // without exception = file already exist
            throw new FileDuplicationException("There is already " + file.getOriginalFilename() + " file!");
        } catch(FileDoesntExistException exc) {
            try {
                File fileToSave = new File(file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                );
                fileValidator.validateFileEntity(fileToSave);

                fileDao.save(fileToSave);

                User currentUser = userDao.findByUsername(username);

                // update user's files
                List<File> userFiles = currentUser.getFiles();
                userFiles.add(fileToSave);
                currentUser.setFiles(userFiles);

                // update user to new files (old files with new added)
                userDao.save(currentUser);      // update operation (ID stays the same)
            } catch(IOException exc1) {
                throw new IllegalArgumentException("Can't extract bytes from uploaded file!");
            }
        }
    }

    @Override
    public void deleteFile(String name) {
        String username = authenticationService.getUsernameOfLoggedInUser();

        File fileToDelete = getFileByName(name);
        User currentUser = userDao.findByUsername(username);

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

        List<File> foundFile = userDao.findByUsername(username).getFiles()
                .stream()
                .parallel()
                .filter(file -> file.getName().equals(name))
                .collect(Collectors.toList());

        if(foundFile.isEmpty()) {
            throw new FileDoesntExistException("File " + name + " doesn't exist!");
        } else {
            return foundFile.get(0);
        }
    }

    @Override
    public List<String> getFileNames() {
        String username = authenticationService.getUsernameOfLoggedInUser();

        List<String> names = userDao.findByUsername(username).getFiles()
                .stream()
                .parallel()
                .map(File::getName)
                .collect(Collectors.toList());

        if(names.isEmpty()) {
            throw new NoFilesException("No files stored in FileCloud!");
        } else {
            return names;
        }
    }

    @Override
    public byte[] getAllFilesInZip() {
        String username = authenticationService.getUsernameOfLoggedInUser();

        List<File> files = userDao.findByUsername(username).getFiles();

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
                throw new NoFilesException("No files stored in FileCloud!");
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
        if(pageNumber < 1) {
            throw new FilePagingException("Page number out of bounds (" + pageNumber + ")!");
        }

        List<String> fileNames = getFileNames();

        PagingInfo pagingInfo = getFilePagingInfo(fileNames.size());
        int pageIndex = pageNumber - 1;

        int startRecordIndex = pageIndex * pagingInfo.getRecordsPerPage();
        if(startRecordIndex >= fileNames.size()) {
            throw new FilePagingException("Too much records selected!");
        }

        // <a; b)
        if(startRecordIndex + pagingInfo.getRecordsPerPage() >= fileNames.size()) {
            return fileNames.subList(startRecordIndex, fileNames.size());
        }

        return fileNames.subList(startRecordIndex, startRecordIndex + pagingInfo.getRecordsPerPage());
    }

    @Override
    public PagingInfo getFilePagingInfo() {
        return getFilePagingInfo(getFileNames().size());
    }

    public PagingInfo getFilePagingInfo(int countOfRecords) {
        PagingInfo pagingInfo = new PagingInfo();

        pagingInfo.setRecordsPerPage(5);
        pagingInfo.setTotalRecords(countOfRecords);
        pagingInfo.setNumberOfPages(
                (int) Math.ceil((((double)pagingInfo.getTotalRecords()) / pagingInfo.getRecordsPerPage()))
        );

        return pagingInfo;
    }
}
