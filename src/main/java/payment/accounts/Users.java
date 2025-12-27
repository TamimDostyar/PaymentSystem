package payment.accounts;

import java.util.Map;
import java.util.HashMap;

public class Users {

    private Integer id;
    private String name;
    private String lastName;
    private String address;
    private String accountType;
    private String username = null;
    private Integer phoneNumber;


    public Users() {}

    public Users(String name, String lastName, String address, String accountType, Integer phoneNumber, String username){
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public Users(Integer id, String name, String lastName, String address, String accountType, Integer phoneNumber, String username){
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }


    public Map<String, Object> getAllData() {
        Map<String, Object> allData = new HashMap<>();
        allData.put("id", this.id);
        allData.put("name", this.name);
        allData.put("lastName", this.lastName);
        allData.put("address", this.address);
        allData.put("accountType", this.accountType);
        allData.put("username", this.username);
        allData.put("phoneNumber", this.phoneNumber);
        return allData;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    // Setters for JSON deserialization
    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
