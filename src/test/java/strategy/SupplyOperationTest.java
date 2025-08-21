package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import db.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplyOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SupplyOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.fruitInventory.clear();
    }

    @Test
    void apply_addToExistingFruit_ok() {
        Storage.set("banana", 100);
        handler.apply("banana", 50);
        assertEquals(150, Storage.get("banana"));
    }

    @Test
    void apply_addNewFruit_ok() {
        handler.apply("orange", 20);
        assertEquals(20, Storage.get("orange"));
    }
}
