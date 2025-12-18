package payment.accounts;
import org.mindrot.jbcrypt.BCrypt;

public class CreateUser extends Users {
    private String password;
    private String confirmPassword;

    
    public CreateUser() {
        super(null, null, null, null, null, null);
    }
    // Constructor with all fields
    public CreateUser(String name, String lastName, String address, String accountType,
                      Integer phoneNumber, String username,
                      String password, String confirmPassword) {
        super(name, lastName, address, accountType, phoneNumber, username);
        this.password = password;
        this.confirmPassword = confirmPassword;
    }



    // // Helper to verify password
    // public static boolean checkPassword(String plaintextPassword, String storedHash) {
    //     return BCrypt.checkpw(plaintextPassword, storedHash);
    // }

    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    // these are from users Get methods
    public String getName() { return super.getName(); }
    public String getLastName() { return super.getLastName(); }
    public String getAddress() { return super.getAddress(); }
    public String getAccountType() { return super.getAccountType(); }
    public String getUsername() { return super.getUsername(); }
    public Integer getPhoneNumber() { return super.getPhoneNumber(); }
    public String getConfirmPassword() {return confirmPassword;}
    public String getPassword() {return password;}
}
