package paymentTest.home;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Map;
import payment.home.Home;

public class HomeTest {
    private Home home;

    @BeforeEach
    public void setUp() {
        home = new Home();
    }

    @Test
    public void homeReturnsSuccessMessage() {
        Map<String, String> result = home.home();
        
        assertNotNull(result, "Result should not be null");
        assertEquals(Map.of("message", "success"), result);
        assertEquals("success", result.get("message"));
    }
}
