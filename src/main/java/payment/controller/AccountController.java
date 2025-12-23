package payment.controller;

import payment.accounts.*;
import payment.database.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {
    UsersDatabase userDatabase = new UsersDatabase();
    AccountDatabase accountDatabase = new AccountDatabase();

    @GetMapping("/db-init")
    public Map<String, String> initializeDB() {
        Map<String, String> msg = new HashMap<>();
        try {
            userDatabase.create_userDB();
            accountDatabase.createAccountTB();
            msg.put("Success", "Successfully initialized");
        } catch (Exception e) {
            msg.put("Error", "Initialization failed");
        }
        return msg;
    }

    @PostMapping("/createAccount/{userID}")
    public Map<String, String> createAccount(
        @RequestBody Accounts account,
        @PathVariable Integer userID) {
        
        Map<String, String> msg = new HashMap<>();
        
        try {
            Accounts newAccount = new Accounts(
                account.getRoutingNumber() != null ? account.getRoutingNumber() : Accounts.randomRoutingNumber(),
                account.getAccountNumber() != null ? account.getAccountNumber() : Accounts.randomAccountNumber(12),
                account.getAmountAvail()
            );

            String result = accountDatabase.createAccountData(newAccount, userID);
            
            if (result.startsWith("Error")) {
                msg.put("error", result);
            } else {
                msg.put("success", result);
                msg.put("accountNumber", newAccount.getAccountNumber());
                msg.put("routingNumber", String.valueOf(newAccount.getRoutingNumber()));
            }
        } catch (Exception e) {
            msg.put("error", "Failed to create account: " + e.getMessage());
        }
        
        return msg;
    }
}
