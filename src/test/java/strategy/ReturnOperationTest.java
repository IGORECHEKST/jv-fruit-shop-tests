package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import db.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReturnOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ReturnOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.fruitInventory.clear();
    }

    @Test
    void apply_returnToExistingStock_ok() {
        Storage.set("apple", 50);
        handler.apply("apple", 10);
        assertEquals(60, Storage.get("apple"));
    }

    @Test
    void apply_returnToNewFruit_ok() {
        handler.apply("grape", 5);
        assertEquals(5, Storage.get("grape"));
    }
}
