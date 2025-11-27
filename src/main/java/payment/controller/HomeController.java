package payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import payment.home.Home;

import java.util.Map;

@RestController
public class HomeController {
    // this is the home that would later display all the endpoints
    @GetMapping("/")
    public Map<String, String> home(){
        Home home = new Home();
        return home.home();
    }
}
