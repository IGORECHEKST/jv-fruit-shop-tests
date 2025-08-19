package impl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileReaderImplTest {
    private static final String TEST_RESOURCES_PATH = "src/test/resources/";
    private static final String VALID_FILE_PATH = TEST_RESOURCES_PATH + "validInput.csv";
    private static final String EMPTY_FILE_PATH = TEST_RESOURCES_PATH + "emptyFile.csv";
    private FileReaderImpl fileReader;

    @BeforeAll
    static void setUpAll() throws IOException {
        Path validFile = Paths.get(VALID_FILE_PATH);
        String content = "type,fruit,quantity" + System.lineSeparator()
                + "b,apple,100" + System.lineSeparator()
                + "s,orange,50" + System.lineSeparator()
                + "b,banana,150" + System.lineSeparator()
                + "p,apple,10" + System.lineSeparator()
                + "r,orange,25" + System.lineSeparator()
                + "p,banana,2" + System.lineSeparator()
                + "s,apple,50" + System.lineSeparator()
                + "p,orange,10" + System.lineSeparator()
                + "p,apple,20";
        Files.createDirectories(validFile.getParent());
        Files.writeString(validFile, content);

        Path emptyFile = Paths.get(EMPTY_FILE_PATH);
        Files.createFile(emptyFile);
    }

    @AfterAll
    static void tearDownAll() throws IOException {
        Files.deleteIfExists(Paths.get(VALID_FILE_PATH));
        Files.deleteIfExists(Paths.get(EMPTY_FILE_PATH));
    }

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
    }

    @Test
    void read_validFile_ok() {
        List<String> lines = fileReader.read(VALID_FILE_PATH);
        assertNotNull(lines);
        assertEquals(9, lines.size());
        assertEquals("b,apple,100", lines.get(0));
        assertEquals("p,apple,20", lines.get(8));
    }

    @Test
    void read_emptyFile_ok() {
        List<String> lines = fileReader.read(EMPTY_FILE_PATH);
        assertNotNull(lines);
        assertEquals(0, lines.size());
    }

    @Test
    void read_nonExistentFile_notOk() {
        String nonExistentPath = TEST_RESOURCES_PATH + "non_existent_file.csv";
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fileReader.read(nonExistentPath));
        assertEquals("Error reading file: " + nonExistentPath, exception.getMessage());
    }

    @Test
    void read_nullFilePath_notOk() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fileReader.read(null));
        assertEquals("File path cannot be null", exception.getMessage());
    }
}
