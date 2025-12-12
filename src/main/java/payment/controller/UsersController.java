package payment.controller;


import payment.database.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            userD.add(user.username());
        }
        return fullInformation;
    }

}
