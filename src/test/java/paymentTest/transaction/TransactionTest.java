package paymentTest.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Map;
import payment.transaction.Transaction;

public class TransactionTest {
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
    }

    @Test
    public void transactionReturnsConnectedStatus() {
        Map<String, String> result = transaction.transaction();
        
        assertNotNull(result, "Result should not be null");
        assertEquals(Map.of("status", "connected to transaction"), result);
        assertEquals("connected to transaction", result.get("status"));
    }
}
