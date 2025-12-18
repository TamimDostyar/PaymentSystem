// this is the simple database that creates information for each users

package payment.database;

import payment.accounts.CreateUser;
import payment.accounts.Users;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDatabase {
    
    private static final String TABLE_NAME = "users";
    
    private Connection getConnection() throws SQLException {
        DBinitialization dbInit = new DBinitialization();
        return dbInit.connect();
    }
    
    /**
     * Initializes the users table if it doesn't exist.
     * This method should be called before performing any operations.
     */
    public void initializeTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "lastName TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "accountType TEXT NOT NULL, " +
                "username TEXT UNIQUE, " +
                "phoneNumber INTEGER NOT NULL, " +
                "password TEXT NOT NULL, " +
                ")";
        
        try (Connection conn = getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null) {
            if (stmt != null) {
                stmt.execute(createTableSQL);
                System.out.println("Users table initialized successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing table: " + e.getMessage());
        }
    }
    
    /**
     * Creates a new user account in the database.
     * @param user The Users object containing user information
     * @return String message indicating success or failure
     */
    public String createUserAccount(CreateUser user) {
        if (user == null) {
            return "Error: User object cannot be null";
        }
        
        String insertSQL = "INSERT INTO " + TABLE_NAME + 
                " (name, lastName, address, accountType, username, phoneNumber, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(insertSQL) : null) {
            
            if (pstmt == null) {
                return "Error: Could not establish database connection";
            }
            
            // Validate passwords match
            if (!user.getPassword().equals(user.getConfirmPassword())){
                return "passwords don't match";
            }
            
            // Hash the password before storing
            String hashedPassword = CreateUser.hashPassword(user.getPassword());
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getAccountType());
            pstmt.setString(5, user.getUsername());
            pstmt.setInt(6, user.getPhoneNumber());
            pstmt.setString(7, hashedPassword);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "User account created successfully for: " + user.getUsername();
            } else {
                return "Error: Failed to create user account";
            }
            

            
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint")) {
                return "Error: Username already exists";
            }
            return "Error creating user account: " + e.getMessage();
        }
    }
    
    /**
     * Retrieves a user by username.
     * @param username The username to search for
     * @return Users object if found, null otherwise
     */
    public Users getUserByUsername(String username) {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(selectSQL) : null) {
            
            if (pstmt == null) {
                return null;
            }
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Users(
                    rs.getString("name"),
                    rs.getString("lastName"),
                    rs.getString("address"),
                    rs.getString("accountType"),
                    rs.getInt("phoneNumber"),
                    rs.getString("username")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all users from the database.
     * @return List of Users objects
     */
    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME;
        
        try (Connection conn = getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(selectSQL) : null) {
            
            if (rs != null) {
                while (rs.next()) {
                    Users user = new Users(
                        rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getString("address"),
                        rs.getString("accountType"),
                        rs.getInt("phoneNumber"),
                        rs.getString("username")
                    );
                    users.add(user);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Updates an existing user account.
     * @param username The username of the user to update
     * @param user The updated Users object
     * @return String message indicating success or failure
     */
    public String updateUserAccount(String username, Users user) {
        if (user == null) {
            return "Error: User object cannot be null";
        }
        
        String updateSQL = "UPDATE " + TABLE_NAME + 
                " SET name = ?, lastName = ?, address = ?, accountType = ?, phoneNumber = ? " +
                "WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(updateSQL) : null) {
            
            if (pstmt == null) {
                return "Error: Could not establish database connection";
            }
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getAccountType());
            pstmt.setInt(5, user.getPhoneNumber());
            pstmt.setString(6, username);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return "User account updated successfully for: " + username;
            } else {
                return "Error: User not found or no changes made";
            }
            
        } catch (SQLException e) {
            return "Error updating user account: " + e.getMessage();
        }
    }
    
    /**
     * Deletes a user account by username.
     * @param username The username of the user to delete
     * @return String message indicating success or failure
     */
    public String deleteUserAccount(String username) {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(deleteSQL) : null) {
            
            if (pstmt == null) {
                return "Error: Could not establish database connection";
            }
            
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                return "User account deleted successfully for: " + username;
            } else {
                return "Error: User not found";
            }
            
        } catch (SQLException e) {
            return "Error deleting user account: " + e.getMessage();
        }
    }
    
    /**
     * Checks if a username already exists in the database.
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String checkSQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(checkSQL) : null) {
            
            if (pstmt == null) {
                return false;
            }
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        
        return false;
    }
}
