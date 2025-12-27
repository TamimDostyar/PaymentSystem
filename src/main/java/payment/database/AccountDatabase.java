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


    public void createAccountTB() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "accountNumber TEXT NOT NULL, " +
                "routingNumber INTEGER NOT NULL CHECK(routingNumber >= 100000000 AND routingNumber <= 999999999), " +
                "amountAvail FLOAT NOT NULL DEFAULT 100.0, " +
                "userID INTEGER NOT NULL, " +
                "FOREIGN KEY(userID) REFERENCES users(id)" +
                ")";
        
        System.out.println("Creating accounts table with SQL: " + createTableSQL);
        
        try (Connection conn = getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to get database connection");
            }
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("Accounts table initialized successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing accounts table: " + e.getMessage());
            throw e;
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

    /**
     * Get account data for a user
     */
    public Accounts getAccountByUserID(Integer userID) {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE userID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(selectSQL) : null) {
            
            if (pstmt == null) {
                return null;
            }
            
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Accounts account = new Accounts(
                    rs.getInt("routingNumber"),
                    rs.getString("accountNumber"),
                    rs.getDouble("amountAvail")
                );
                return account;
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving account: " + e.getMessage());
        }
        
        return null;
    }
}
