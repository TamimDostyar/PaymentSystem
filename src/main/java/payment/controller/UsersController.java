package payment.controller;


import payment.accounts.*;
import payment.database.*;

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


    // these information later needs to come from database upon the user is verified
    @GetMapping("/user-information")
    public Map<String, String> allInformation(
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String phonenumber,
            @RequestParam String accountType,
            @RequestParam String address) {
        Map<String, String> fullInformation = new HashMap<>();

        List<String> userD = new ArrayList<>();
        
        for (var user : userDatabase.getAllUsers()) {
            userD.add(user.getUsername());
        }
        return fullInformation;
    }


    @GetMapping("/db-init")
    public Map<String, String> initializeDB() {
        Map<String, String> msg = new HashMap<>();
        try {
            userDatabase.initializeTable();
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
    

}
