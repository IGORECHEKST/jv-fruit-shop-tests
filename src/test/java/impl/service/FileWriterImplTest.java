package impl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class FileWriterImplTest {
    private static final String OUTPUT_FILE_PATH = "src/test/resources/testOutput.csv";
    private FileWriterImpl fileWriter;

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(OUTPUT_FILE_PATH));
    }

    @Test
    void write_validDataAndPath_ok() throws IOException {
        fileWriter = new FileWriterImpl();
        String testData = "fruit,quantity" + System.lineSeparator()
                + "banana,100"
                + System.lineSeparator();
        Path path = Paths.get(OUTPUT_FILE_PATH);
        Files.createDirectories(path.getParent());
        fileWriter.write(testData, OUTPUT_FILE_PATH);
        assertTrue(Files.exists(path));
        assertEquals(testData, Files.readString(path));
    }

    @Test
    void write_nullData_notOk() {
        fileWriter = new FileWriterImpl();
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fileWriter.write(null, OUTPUT_FILE_PATH));
        assertEquals("Data to write cannot be null", exception.getMessage());
    }

    @Test
    void write_nullFilePath_notOk() {
        fileWriter = new FileWriterImpl();
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fileWriter.write("some data", null));
        assertEquals("File path cannot be null", exception.getMessage());
    }

    @Test
    void write_invalidPath_notOk() {
        fileWriter = new FileWriterImpl();
        String invalidPath = "/invalid-dir/file.csv";
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fileWriter.write("test data", invalidPath));
        assertTrue(exception.getMessage().contains("Error writing to file"));
    }
}
