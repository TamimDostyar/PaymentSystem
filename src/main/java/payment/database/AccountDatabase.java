/**
 * @author: Tamim Dostyar
 * This page only shows account information including transactions, account money, and a few other information like routing, accountNumber
 */

package payment.database;

import java.sql.*;
import payment.accounts.*;;

public class AccountDatabase {
    private static final String TABLE_NAME = "accounts";

    private Connection getConnection() throws SQLException {
        DBinitialization dbInit = new DBinitialization();
        return dbInit.connect();
    }


    public void createAccountTB() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "accountNumber TEXT NOT NULL, " +
                "routingNumber INTEGER NOT NULL CHECK(routingNumber >= 100000000 AND routingNumber <= 999999999), " +
                "amountAvail FLOAT, " +
                "userID INTEGER NOT NULL, " +
                "FOREIGN KEY(userID) REFERENCES users(id)" +
                ")";
        
        try (Connection conn = getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null) {
            if (stmt != null) {
                stmt.execute(createTableSQL);
                System.out.println("Accounts table initialized successfully with account/routing number limits.");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing table: " + e.getMessage());
        }
    }

    public String createAccountData(Accounts account, Integer userID) {
        if (account == null) {
            return "Error: User object cannot be null";
        }

        String insertSQL = "INSERT INTO " + TABLE_NAME +
                " (accountNumber, routingNumber, amountAvail, userID) " +
                "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(insertSQL) : null) {
            
            if (pstmt == null) {
                return "Error: Could not establish database connection";
            }

            pstmt.setString(1, account.getAccountNumber());
            pstmt.setInt(2, account.getRoutingNumber());
            pstmt.setDouble(3, account.getAmountAvail());
            pstmt.setInt(4, userID);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return "User account created successfully for: " + userID;
            } else {
                return "Error: Failed to create user account";
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint")) {
                return "Error: Username already exists";
            }
            return "Error creating user account: " + e.getMessage();
        }
    }


}
