package impl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.FruitTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.OperationHandler;

class ShopServiceImplTest {
    private Map<FruitTransaction.Operation, OperationHandler> operationHandlers;
    private ShopServiceImpl shopService;
    private TestOperationHandler balanceHandler;
    private TestOperationHandler supplyHandler;

    @BeforeEach
    void setUp() {
        balanceHandler = new TestOperationHandler();
        supplyHandler = new TestOperationHandler();
        operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, balanceHandler);
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, supplyHandler);
        shopService = new ShopServiceImpl(operationHandlers);
    }

    @Test
    void process_validTransactions_ok() {
        List<FruitTransaction> transactions = Stream.of(
                new FruitTransaction(FruitTransaction.Operation.BALANCE, "banana", 100),
                new FruitTransaction(FruitTransaction.Operation.SUPPLY, "apple", 50)
        ).collect(Collectors.toList());

        shopService.process(transactions);

        assertEquals("banana", balanceHandler.getFruit());
        assertEquals(100, balanceHandler.getQuantity());

        assertEquals("apple", supplyHandler.getFruit());
        assertEquals(50, supplyHandler.getQuantity());
    }

    @Test
    void process_unsupportedOperation_notOk() {
        List<FruitTransaction> transactions = List.of(
                new FruitTransaction(FruitTransaction.Operation.PURCHASE, "apple", 10)
        );
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shopService.process(transactions));
        assertEquals("No handler found for operation: PURCHASE", exception.getMessage());
    }

    @Test
    void process_nullTransactions_notOk() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shopService.process(null));
        assertEquals("Transactions list cannot be null", exception.getMessage());
    }

    private static class TestOperationHandler implements OperationHandler {
        private String fruit;
        private int quantity;

        @Override
        public void apply(String fruit, int quantity) {
            this.fruit = fruit;
            this.quantity = quantity;
        }

        public String getFruit() {
            return fruit;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
