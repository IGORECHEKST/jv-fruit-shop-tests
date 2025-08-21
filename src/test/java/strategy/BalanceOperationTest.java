package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import db.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BalanceOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new BalanceOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.fruitInventory.clear();
    }

    @Test
    void apply_initialBalance_ok() {
        handler.apply("banana", 100);
        assertEquals(100, Storage.get("banana"));
    }

    @Test
    void apply_overrideBalance_ok() {
        Storage.set("apple", 50);
        handler.apply("apple", 150);
        assertEquals(150, Storage.get("apple"));
    }
}
