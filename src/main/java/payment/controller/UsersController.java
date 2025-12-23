package payment.controller;


import payment.accounts.*;
import payment.database.*;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
@RestController
public class UsersController {

    // user database 
    UsersDatabase userDatabase = new UsersDatabase();

    @GetMapping("/db-init")
    public Map<String, String> initializeDB() {
        Map<String, String> msg = new HashMap<>();
        try {
            userDatabase.create_userDB();
            msg.put("Success", "Successfully initialized");
        } catch (Exception e) {
            msg.put("Error", "Initialization failed");
        }
        return msg;
    }

    @PostMapping("/createUser")
    public Map<String, String> createUsers(@RequestBody CreateUser user) {
        Map<String, String> msg = new HashMap<>();
        try {
            CreateUser userData = new CreateUser(
                user.getName(),
                user.getLastName(),
                user.getAddress(),
                user.getAccountType(),
                user.getPhoneNumber(),
                user.getUsername(),
                user.getPassword(),
                user.getConfirmPassword()
            );
            userDatabase.createUserAccount(userData);




            msg.put("Success", "User created Successfully");
        } catch (Exception e) {
            msg.put("Error", "User could not be saved");
        }
        return msg;
    }
    
    @PostMapping("/get-data-byUsername")
    public Map<String, String> getUserDataByUserName(@RequestBody String username) {
        Map<String, String> userData = new HashMap<>();
        try {
            if (username == null || username.isBlank()) {
                userData.put("Error", "Username cannot be null or blank");
                return userData;
            }
            Users data = userDatabase.getUserByUsername(username);
            if (data == null) {
                userData.put("Error", "User not found");
                return userData;
            }
            userData.put("name", data.getName());
            userData.put("lastName", data.getLastName());
            userData.put("address", data.getAddress());
            userData.put("accountType", data.getAccountType());
            userData.put("Phone Number", data.getPhoneNumber().toString());
            return userData;
        } catch (Exception e) {
            userData.put("Error", "Something went wrong: " + e.getMessage());
            return userData;
        }
    }

    @PostMapping("/delete-data-byUsername")
    public Map<String, String> deleteDataByUserName(@RequestBody String username) {
        Map<String, String> userData = new HashMap<>();
        try {
            if (username == null || username.isBlank()) {
                userData.put("Error", "Username cannot be null or blank");
                return userData;
            }
            String deleteResult = userDatabase.deleteUserAccount(username);
            if (deleteResult.startsWith("User account deleted successfully")) {
                userData.put("Success", "User is deleted");
            } else {
                userData.put("Error", deleteResult);
            }
            return userData;
        } catch (Exception e) {
            userData.put("Error", "Something went wrong: " + e.getMessage());
            return userData;
        }
    }

    @PostMapping("/user-data-exist")
    public Map<String, String> userNameExist(@RequestBody String username) {
        Map<String, String> userData = new HashMap<>();
        try {
            if (username == null || username.trim().isEmpty()) {
                userData.put("Error", "Username cannot be null or blank");
                return userData;
            }
            boolean userExists = userDatabase.usernameExists(username.trim());
            if (userExists) {
                userData.put("Exists", "true");
                userData.put("Message", "User exists");
            } else {
                userData.put("Exists", "false");
                userData.put("Message", "User not available");
            }
            return userData;
        } catch (Exception e) {
            userData.put("Error", "Something went wrong: " + e.getMessage());
            return userData;
        }
    }

}
