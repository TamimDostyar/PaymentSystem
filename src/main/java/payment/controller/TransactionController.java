package payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import payment.accounts.Accounts;
import payment.accounts.Transaction;
import payment.database.TransactionDatabase;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TransactionController {
    
    TransactionDatabase transactionDatabase = new TransactionDatabase();

    /**
     * Initialize the transactions table
     */
    @GetMapping("/transaction/init")
    public Map<String, String> initTransactionTable() {
        Map<String, String> msg = new HashMap<>();
        try {
            transactionDatabase.createTransactionTB();
            msg.put("success", "Transaction table initialized successfully");
        } catch (Exception e) {
            msg.put("error", "Failed to initialize transaction table: " + e.getMessage());
        }
        return msg;
    }

    /**
     * Create a transaction record for an account
     * Request body: { "description": "...", "transAmount": 100, "date": "...", "type": "DEBIT/CREDIT", "status": "COMPLETE/FAIL/PENDING" }
     */
    @PostMapping("/transaction/create/{accountID}")
    public Map<String, String> createTransaction(
            @RequestBody Map<String, Object> request,
            @PathVariable Integer accountID) {
        
        Map<String, String> msg = new HashMap<>();
        try {
            String description = (String) request.get("description");
            Integer transAmount = (Integer) request.get("transAmount");
            String type = (String) request.get("type");
            String status = (String) request.get("status"); // COMPLETE, FAIL, or PENDING

            Transaction transaction = new Transaction(
                description,
                transAmount,
                Transaction.getCurrentDate(),
                type,
                status != null ? status : "PENDING"
            );

            String result = transactionDatabase.createTransaction(transaction, accountID, status);
            if (result.startsWith("Error")) {
                msg.put("error", result);
            } else {
                msg.put("success", result);
            }
        } catch (Exception e) {
            msg.put("error", "Failed to create transaction: " + e.getMessage());
        }
        return msg;
    }

    /**
     * Transfer money between accounts
     * Request body for TransferRequest:
     * {
     *   "fromAccountNumber": "123456789012",
     *   "fromRoutingNumber": 111222333,
     *   "toAccountNumber": "987654321098",
     *   "toRoutingNumber": 444555666,
     *   "amount": 500,
     *   "description": "Payment for services"
     * }
     */
    @PostMapping("/transaction/transfer")
    public Map<String, String> makeTransfer(@RequestBody Map<String, Object> transferRequest) {
        Map<String, String> msg = new HashMap<>();
        try {
            // Extract sender account info
            String fromAccountNumber = (String) transferRequest.get("fromAccountNumber");
            Integer fromRoutingNumber = (Integer) transferRequest.get("fromRoutingNumber");
            
            // Extract recipient account info
            String toAccountNumber = (String) transferRequest.get("toAccountNumber");
            Integer toRoutingNumber = (Integer) transferRequest.get("toRoutingNumber");
            
            // Extract amount and description
            Integer amount = (Integer) transferRequest.get("amount");
            String description = (String) transferRequest.get("description");

            // Validate required fields
            if (fromAccountNumber == null || fromRoutingNumber == null) {
                msg.put("error", "Sender account number and routing number are required");
                return msg;
            }
            if (toAccountNumber == null || toRoutingNumber == null) {
                msg.put("error", "Recipient account number and routing number are required");
                return msg;
            }
            if (amount == null || amount <= 0) {
                msg.put("error", "Amount must be greater than 0");
                return msg;
            }

            // Create sender account object
            Accounts senderAccount = new Accounts(fromRoutingNumber, fromAccountNumber, 0);

            // Create transaction with recipient info (status will be set to COMPLETE on success)
            Transaction transaction = new Transaction(
                description != null ? description : "Transfer",
                amount,
                "TRANSFER",
                toAccountNumber,
                toRoutingNumber,
                "PENDING"
            );

            // Execute the transfer
            String result = transactionDatabase.makeTransaction(transaction, senderAccount);
            
            if (result.startsWith("Error")) {
                msg.put("error", result);
            } else {
                msg.put("success", result);
                msg.put("amount", String.valueOf(amount));
                msg.put("from", fromAccountNumber);
                msg.put("to", toAccountNumber);
            }
        } catch (Exception e) {
            msg.put("error", "Failed to process transfer: " + e.getMessage());
        }
        return msg;
    }

    @PostMapping("/user-transaction")
    public Map<String, String> transactionInfo(@RequestBody Map<String, Object> request){
        Map<String, String> response = new HashMap<>();

        try {
            if (request == null || !request.containsKey("userID")) {
                response.put("Error", "userID is required");
                return response;
            }
            Integer userID;
            try {
                userID = (request.get("userID") instanceof Integer)
                        ? (Integer) request.get("userID")
                        : Integer.valueOf(request.get("userID").toString());
            } catch (Exception ex) {
                response.put("Error", "Invalid userID format");
                return response;
            }

            if (userID == null || userID == 0) {
                response.put("Error", "userID cannot be null or zero");
                return response;
            }

            String data = transactionDatabase.getTransactionData(userID);

            if (data == null || data.trim().isEmpty() || data.startsWith("No transaction data")) {
                response.put("Success", "True");
                response.put("info", "No transaction data found for the given user ID.");
            } else if (data.startsWith("Error")) {
                response.put("Error", data);
            } else {
                response.put("Success", "True");
                response.put("info", data);
            }
            return response;
        } catch (Exception e){
            response.put("Error", "Something went wrong: " + e.getMessage());
            return response;
        }
    }
}