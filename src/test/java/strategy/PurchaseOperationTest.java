package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import db.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseOperationTest {
    private OperationHandler handler;

    @BeforeEach
    void setUp() {
        Storage.getAll().clear();
        handler = new PurchaseOperation();
    }

    @AfterEach
    void tearDown() {
        Storage.getAll().clear();
    }

    @Test
    void apply_validPurchase_ok() {
        Storage.set("banana", 100);
        handler.apply("banana", 25);
        assertEquals(75, Storage.get("banana"));
    }

    @Test
    void apply_purchaseMoreThanAvailable_notOk() {
        Storage.set("apple", 50);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> handler.apply("apple", 60));
        assertEquals("Not enough apple in stock to complete purchase of 60",
                exception.getMessage());
    }

    @Test
    void apply_purchaseFromEmptyStock_notOk() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> handler.apply("orange", 10));
        assertEquals("Not enough orange in stock to complete purchase of 10",
                exception.getMessage());
    }
}
