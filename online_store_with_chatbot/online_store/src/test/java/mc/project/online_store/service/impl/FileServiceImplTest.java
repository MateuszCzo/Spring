package mc.project.online_store.service.impl;

import mc.project.online_store.exception.FileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class FileServiceImplTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Value("${file.path.test}")
    private String filePath;

    @Autowired
    private FileServiceImpl fileService;

    private Path parentDir;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        parentDir = Paths.get(filePath);
        if (Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        }
    }

    @Test
    public void givenValidFileAndFilePath_whenCreateFile_thenTransfersFile() throws IOException {
        Path testFilePath = Files.createTempFile(parentDir, "testFile", ".txt");
        MultipartFile mockFile = new MockMultipartFile(
                "testFile",
                "testFile.txt",
                "text/plain",
                "This is a test file".getBytes()
        );

        try {
            fileService.postFile(mockFile, testFilePath.toString());

            assertTrue(Files.exists(testFilePath));
            assertEquals("This is a test file", Files.readString(testFilePath));
        } finally {
            if (Files.exists(testFilePath)) {
                Files.delete(testFilePath);
            }
        }
    }

    @Test
    public void givenInvalidFileAndFilePath_whenCreateFile_thenThrowsFileException() throws IOException {
        Path testFilePath = Files.createTempFile(parentDir, "testFile", ".txt");
        MultipartFile mockFile = new MockMultipartFile(
                "testFile",
                "testFile.txt",
                "text/plain",
                "This is a test file".getBytes()
        );
        String invalidPath = testFilePath.resolve("invalidDir").toString();

        try {
            assertThrows(FileException.class, () -> fileService.postFile(mockFile, invalidPath));
        } finally {
            if (Files.exists(testFilePath)) {
                Files.delete(testFilePath);
            }
        }
    }

    @Test
    public void givenValidPath_whenGetFile_thenReturnsResource() throws IOException {
        Path testFilePath = Files.createTempFile(parentDir, "testFile", ".txt");
        Files.writeString(testFilePath, "This is a test file");

        try {
            Resource resource = fileService.getFile(testFilePath.toString());

            assertTrue(resource.exists());
            assertTrue(resource.isReadable());
            try (InputStream inputStream = resource.getInputStream()) {
                assertEquals("This is a test file", new String(inputStream.readAllBytes()));
            }
        } finally {
            if (Files.exists(testFilePath)) {
                Files.delete(testFilePath);
            }
        }
    }

    @Test
    public void givenInvalidPath_whenGetFile_thenThrowsFileException() throws IOException {
        Path testFilePath = Files.createTempFile(parentDir, "testFile", ".txt");
        String invalidPath = testFilePath.resolve("nonExistentFile.txt").toString();

        try {
            assertThrows(FileException.class, () -> fileService.getFile(invalidPath));
        } finally {
            if (Files.exists(testFilePath)) {
                Files.delete(testFilePath);
            }
        }
    }

    @Test
    public void givenValidPath_whenDeleteFile_thenDeletesFileAndReturnsVoid() throws IOException {
        Path testFilePath = Files.createTempFile(parentDir, "testFile", ".txt");
        Files.writeString(testFilePath, "This is a test file");

        try {
            fileService.deleteFile(testFilePath.toString());

            assertFalse(Files.exists(testFilePath));
        } finally {
            if (Files.exists(testFilePath)) {
                Files.delete(testFilePath);
            }
        }
    }

    @Test
    public void givenInvalidPath_whenDeleteFile_thenReturnsVoid() throws IOException {
        Path testFilePath = Files.createTempFile(parentDir, "testFile", ".txt");
        String invalidPath = testFilePath.resolve("nonExistentFile.txt").toString();

        try {
            fileService.deleteFile(invalidPath);

            assertFalse(Files.exists(Path.of(invalidPath)));
        } finally {
            if (Files.exists(testFilePath)) {
                Files.delete(testFilePath);
            }
        }
    }
}