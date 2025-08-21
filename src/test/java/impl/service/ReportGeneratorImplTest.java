package impl.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import db.Storage;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private ReportGeneratorImpl reportGenerator;

    @BeforeEach
    void setUp() {
        Map<String, Integer> inventoryCopy = Storage.fruitInventory;
        for (String fruit : inventoryCopy.keySet()) {
            Storage.set(fruit, 0);
        }
        reportGenerator = new ReportGeneratorImpl();
    }

    @AfterEach
    void tearDown() {
        Storage.fruitInventory.clear();
    }

    @Test
    void generateReport_multipleFruits_ok() {
        Storage.set("banana", 152);
        Storage.set("apple", 90);
        String actualReport = reportGenerator.generateReport();
        assertTrue(actualReport.contains("fruit,quantity" + System.lineSeparator()));
        assertTrue(actualReport.contains("apple,90" + System.lineSeparator()));
        assertTrue(actualReport.contains("banana,152" + System.lineSeparator()));
    }

    @Test
    void generateReport_emptyStorage_ok() {
        String expectedReport = "fruit,quantity"
                + System.lineSeparator();
        String actualReport = reportGenerator.generateReport();
        assertEquals(expectedReport, actualReport);
    }
}
