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
    
    public String getName(){
        return this.name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getAddress(){
        return this.address;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public String getUsername() {
        return this.username;
    }

    public Integer getPhoneNumber() {
        return this.phoneNumber;
    }
}
