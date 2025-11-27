package payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import payment.transaction.Transaction;

import java.util.Map;

@RestController
public class TransactionController {

    @GetMapping("/transaction")
    public Map<String, String> transaction(){
        Transaction transaction = new Transaction();
        return transaction.transaction();
    }
}