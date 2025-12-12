package payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import payment.database.DBinitialization;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DBInitializationController {

    @GetMapping("/databaseInitialize")
    public Map<String, String> databaseInitialize() {
        DBinitialization dbInit = new DBinitialization();
        Map<String, String> result = new HashMap<>();
        
        try {
            Connection connection = dbInit.connect();
            if (connection != null) {
                connection.close();
                result.put("message", "Connection to SQLite has been established.");
                result.put("status", "success");
            } else {
                result.put("message", "Failed to establish database connection");
                result.put("status", "error");
            }
        } catch (SQLException e) {
            result.put("message", "There was an error: " + e.getMessage());
            result.put("status", "error");
        }
        
        return result;
    }
}