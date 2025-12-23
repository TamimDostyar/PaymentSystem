//Transaction = Transfer money

package payment.database;

import java.sql.*;
import payment.accounts.*;;

public class TransactionDatabase {
    private static final String TABLE_NAME = "transactions";

    private Connection getConnection() throws SQLException {
        DBinitialization dbInit = new DBinitialization();
        return dbInit.connect();
    }


    /**
     * Initializes the transactions table if it doesn't exist.
     * This method should be called before performing any operations.
     */
    // Transaction status constants
    public static final String STATUS_COMPLETE = "COMPLETE";
    public static final String STATUS_FAIL = "FAIL";
    public static final String STATUS_PENDING = "PENDING";

    public void createTransactionTB() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "transAmount FLOAT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "description TEXT, " +
                "accountID INTEGER NOT NULL, " +
                "status TEXT NOT NULL DEFAULT 'PENDING', " +
                "FOREIGN KEY(accountID) REFERENCES accounts(id)" +
                ")";
        
        try (Connection conn = getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null) {
            if (stmt != null) {
                stmt.execute(createTableSQL);
                System.out.println("Transactions table initialized successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing transaction table: " + e.getMessage());
        }
    }

    public String createTransaction(Transaction transaction, Integer accountID, String status) {
        if (transaction == null) {
            return "Error: Transaction object cannot be null";
        }

        // Default to PENDING if status not provided
        if (status == null || status.isEmpty()) {
            status = STATUS_PENDING;
        }

        String insertSQL = "INSERT INTO " + TABLE_NAME +
                " (transAmount, date, type, description, accountID, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(insertSQL) : null) {

            if (pstmt == null) {
                return "Error: Could not establish database connection";
            }

            pstmt.setInt(1, transaction.getTransAmount());
            pstmt.setString(2, transaction.getDate());
            pstmt.setString(3, transaction.getType());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setInt(5, accountID);
            pstmt.setString(6, status);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return "Transaction created successfully for accountID: " + accountID;
            } else {
                return "Error: Failed to create transaction";
            }
        } catch (SQLException e) {
            return "Error creating transaction: " + e.getMessage();
        }
    }

    public String makeTransaction(Transaction transaction, Accounts account){
        if (transaction == null || account == null) {
            return "Error: Transaction and Account cannot be null";
        }

        String toAccountNumber = transaction.getToAccountNumber();
        Integer toRoutingNumber = transaction.getToRoutingNumber();
        Integer amount = transaction.getTransAmount();

        if (toAccountNumber == null || toRoutingNumber == null) {
            return "Error: Recipient account number and routing number are required";
        }

        if (amount == null || amount <= 0) {
            return "Error: Transaction amount must be greater than 0";
        }

        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null) {
                return "Error: Could not establish database connection";
            }
            
            // Start transaction for atomicity
            conn.setAutoCommit(false);

            // Find sender's account ID and balance from database
            String findSenderSQL = "SELECT id, amountAvail FROM accounts WHERE accountNumber = ? AND routingNumber = ?";
            Integer senderAccountId = null;
            double senderBalance = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(findSenderSQL)) {
                pstmt.setString(1, account.getAccountNumber());
                pstmt.setInt(2, account.getRoutingNumber());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    senderAccountId = rs.getInt("id");
                    senderBalance = rs.getDouble("amountAvail");
                } else {
                    conn.rollback();
                    return "Error: Sender account not found";
                }
            }

            // Check if sender has sufficient funds (from database)
            if (senderBalance < amount) {
                conn.rollback();
                return "Error: Insufficient funds. Available: " + senderBalance + ", Required: " + amount;
            }

            // Find recipient's account ID
            String findRecipientSQL = "SELECT id, amountAvail FROM accounts WHERE accountNumber = ? AND routingNumber = ?";
            Integer recipientAccountId = null;
            try (PreparedStatement pstmt = conn.prepareStatement(findRecipientSQL)) {
                pstmt.setString(1, toAccountNumber);
                pstmt.setInt(2, toRoutingNumber);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    recipientAccountId = rs.getInt("id");
                } else {
                    conn.rollback();
                    return "Error: Recipient account not found";
                }
            }

            // Deduct from sender's account
            String deductSQL = "UPDATE accounts SET amountAvail = amountAvail - ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deductSQL)) {
                pstmt.setInt(1, amount);
                pstmt.setInt(2, senderAccountId);
                pstmt.executeUpdate();
            }

            // Add to recipient's account
            String addSQL = "UPDATE accounts SET amountAvail = amountAvail + ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(addSQL)) {
                pstmt.setInt(1, amount);
                pstmt.setInt(2, recipientAccountId);
                pstmt.executeUpdate();
            }

            // Record transaction for sender (debit) with COMPLETE status
            String insertTransSQL = "INSERT INTO " + TABLE_NAME +
                    " (transAmount, date, type, description, accountID, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertTransSQL)) {
                pstmt.setInt(1, -amount); // Negative for debit
                pstmt.setString(2, transaction.getDate());
                pstmt.setString(3, "DEBIT");
                pstmt.setString(4, transaction.getDescription() != null ? 
                        transaction.getDescription() : "Transfer to " + toAccountNumber);
                pstmt.setInt(5, senderAccountId);
                pstmt.setString(6, STATUS_COMPLETE);
                pstmt.executeUpdate();
            }

            // Record transaction for recipient (credit) with COMPLETE status
            try (PreparedStatement pstmt = conn.prepareStatement(insertTransSQL)) {
                pstmt.setInt(1, amount); // Positive for credit
                pstmt.setString(2, transaction.getDate());
                pstmt.setString(3, "CREDIT");
                pstmt.setString(4, transaction.getDescription() != null ? 
                        transaction.getDescription() : "Transfer from " + account.getAccountNumber());
                pstmt.setInt(5, recipientAccountId);
                pstmt.setString(6, STATUS_COMPLETE);
                pstmt.executeUpdate();
            }

            // Commit transaction
            conn.commit();
            return "Transaction successful! Transferred $" + amount + " to account " + toAccountNumber;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    return "Error during rollback: " + rollbackEx.getMessage();
                }
            }
            return "Error making transaction: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Log error but don't throw
                }
            }
        }
    }


    public String getTransactionData(Integer userID) {
        if (userID == null || userID == 0) {
            return "UserID can't be 0 or empty";
        }

        StringBuilder result = new StringBuilder();
        String sqlQuery = "SELECT t.transAmount, t.date, t.type, t.description, t.accountID, t.status, " +
                "a.accountNumber, a.routingNumber " +
                "FROM " + TABLE_NAME + " t " +
                "JOIN accounts a ON t.accountID = a.id " +
                "WHERE a.userID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, userID);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    int transAmount = rs.getInt("transAmount");
                    String date = rs.getString("date");
                    String type = rs.getString("type");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    String accountNumber = rs.getString("accountNumber");
                    result.append("Transaction: ")
                        .append("Amount: $").append(transAmount)
                        .append(", Date: ").append(date)
                        .append(", Type: ").append(type)
                        .append(", Status: ").append(status)
                        .append(", Description: ").append(description)
                        .append(", Account: ").append(accountNumber)
                        .append("\n");
                }
                if (!hasData) {
                    return "No transaction data found for the given user ID.";
                }
            }
        } catch (SQLException e) {
            return "Error retrieving transaction data: " + e.getMessage();
        }

        return result.toString();
    }
}
