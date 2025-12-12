// package paymentTest.home;

// import org.junit.jupiter.api.Test;

// import payment.accounts.Users;

// import org.junit.jupiter.api.BeforeEach;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import java.util.Map;

// public class UserAccountTest {
//     private Users users;

//     @BeforeEach
//     public void setUp() {users = new Users();}

//     @Test
//     public void userAccountReturnsSuccessMessage() {
//         Map<String, String> result = users.useraccount();
        
//         assertNotNull(result, "Result should not be null");
//         assertEquals(Map.of("message", "success"), result);
//         assertEquals("success", result.get("message"));
//     }
// }
