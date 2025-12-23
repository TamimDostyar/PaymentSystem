package payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Main {
    @GetMapping("/")
    public Map<String, String> m_component() {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Successfully connected to the main component");
        msg.put("status", "success");
        return msg;
    }

    @GetMapping("/developer")
    public Map<String, Map<String, String>> developer_info() {

        Map<String, String> info = new HashMap<>();
        info.put("Success", "successfully connect");
        
        Map<String, String> user = new HashMap<>();
        user.put("Name", "Tamim Dostyar");
        user.put("Occupation", "Software Engineer");
        user.put("Contact", "a.tamimdostyar@gmail.com");

        Map<String, Map<String, String>> finalmsg = new HashMap<>();
        finalmsg.put("info", user);

        return finalmsg;
    }
}
