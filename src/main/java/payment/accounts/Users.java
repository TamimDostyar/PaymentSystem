package payment.accounts;

import java.util.Map;
import java.util.HashMap;

public class Users {

    private String name;
    private String lastName;
    private String address;
    private String accountType;
    private String username = null;
    private Integer phoneNumber;

    public Users(String name, String lastName, String address, String accountType, Integer phoneNumber, String username){
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public Map<String, Object> getAllData() {
        Map<String, Object> allData = new HashMap<>();
        allData.put("name", this.name);
        allData.put("lastName", this.lastName);
        allData.put("address", this.address);
        allData.put("accountType", this.accountType);
        allData.put("username", this.username);
        allData.put("phoneNumber", this.phoneNumber);
        return allData;
    }

    public String name(){
        return this.name;
    }

    public String lastName() {
        return this.lastName;
    }

    public String address(){
        return this.address;
    }

    public String accountType() {
        return this.accountType;
    }

    public String username() {
        return this.username;
    }

    public Integer phoneNumber() {
        return this.phoneNumber;
    }
}
