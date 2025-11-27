package paymentTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import payment.Main;

public class MainTest {

    @Test
    public void mainReturnsHelloWorld() {
        Main main = new Main();
        String result = main.paymentMain();
        assertEquals("Hello Developers", result);
    }
}
