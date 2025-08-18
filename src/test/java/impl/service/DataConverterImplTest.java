package impl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.FruitTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataConverterImplTest {
    private DataConverterImpl dataConverter;

    @BeforeEach
    void setUp() {
        dataConverter = new DataConverterImpl();
    }

    @Test
    void convert_validLines_ok() {
        List<String> lines = Stream.of(
                "b,banana,100",
                "s,apple,50"
        ).collect(Collectors.toList());
        List<FruitTransaction> transactions = dataConverter.convert(lines);
        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        assertEquals(FruitTransaction.Operation.BALANCE, transactions.get(0).getOperation());
        assertEquals("banana", transactions.get(0).getFruit());
        assertEquals(100, transactions.get(0).getQuantity());

        assertEquals(FruitTransaction.Operation.SUPPLY, transactions.get(1).getOperation());
        assertEquals("apple", transactions.get(1).getFruit());
        assertEquals(50, transactions.get(1).getQuantity());
    }

    @Test
    void convert_nullLines_notOk() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dataConverter.convert(null));
        assertEquals("Input lines cannot be null", exception.getMessage());
    }

    @Test
    void parseLine_invalidFormat_notOk() {
        List<String> invalidLines = Stream.of(
                "b,banana",
                "s,apple,50,extra"
        ).collect(Collectors.toList());

        assertThrows(RuntimeException.class, () -> dataConverter.convert(invalidLines));
    }

    @Test
    void parseLine_invalidQuantity_notOk() {
        List<String> invalidLines = Stream.of("b,apple,abc").collect(Collectors.toList());
        assertThrows(RuntimeException.class, () -> dataConverter.convert(invalidLines));
    }

    @Test
    void parseLine_negativeQuantity_notOk() {
        List<String> invalidLines = Stream.of("p,apple,-10").collect(Collectors.toList());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> dataConverter.convert(invalidLines));
        assertEquals("Quantity cannot be negative: -10", exception.getMessage());
    }

    @Test
    void parseLine_invalidOperationCode_notOk() {
        List<String> invalidLines = Stream.of("x,apple,10").collect(Collectors.toList());
        assertThrows(IllegalArgumentException.class, () -> dataConverter.convert(invalidLines));
    }
}
