package com.gjm.file_cloud.service;

import com.gjm.file_cloud.dao.FileDao;
import com.gjm.file_cloud.entity.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FileServiceDatabaseImplTest {
    @Mock
    private FileDao fileDaoMock;

    private FileService fileServiceMock;

    private File file;

    // TODO: write tests for getFileNames() and getAllFilesInZip() which will test the logic not only Mocks, need @Autowired dao for this
//    @Autowired
//    private FileDao fileDaoReal;
//
//    private FileService fileServiceReal;

    @Before
    public void setUp() throws Exception {
//        fileServiceMock = new FileServiceDatabaseImpl(fileDaoMock);
//        fileServiceReal = new FileServiceImpl(fileDaoReal);

        file = new File();
        file.setBytes(null);
        file.setName("Example_file");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addFile() {
//        fileServiceMock.addFile(file);

        Mockito.verify(fileDaoMock, Mockito.times(1))
                .save(file);
    }

    @Test
    public void deleteFile() {
//        fileServiceMock.deleteFile(file.getName());

        Mockito.verify(fileDaoMock, Mockito.times(1))
                .deleteFileByName(file.getName());
    }

    @Test
    public void getFileByName() {
//        fileServiceMock.getFileByName(file.getName());

        Mockito.verify(fileDaoMock, Mockito.times(1))
                .findFileByName(file.getName());
    }

    @Test
    public void getFileNames() {
//        fileServiceMock.getFileNames();

        Mockito.verify(fileDaoMock, Mockito.times(1))
                .findAll();

//        fileServiceReal.addFile(file);
//
//        List<String> fileNames = fileServiceReal.getFileNames();
//
//        Assert.assertEquals(fileNames.size(), 1);
//        Assert.assertEquals(fileNames.get(0), file.getName());
    }

    @Test
    public void getAllFilesInZip() {
//        fileServiceMock.getAllFilesInZip();

        Mockito.verify(fileDaoMock, Mockito.times(1))
                .findAll();
    }
}