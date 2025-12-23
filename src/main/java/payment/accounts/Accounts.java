package payment.accounts;

import java.security.SecureRandom;

public class Accounts {
    private Integer routingNumber;
    private String accountNumber;
    private double amountAvail;
    private static final SecureRandom random = new SecureRandom();

    // Constructors
    public Accounts(Integer routingNumber, String accountNumber, double amountAvail) {
        this.routingNumber = routingNumber;
        this.accountNumber = accountNumber;
        this.amountAvail = amountAvail;
    }

    public Accounts() {
        this.routingNumber = randomRoutingNumber();
        this.accountNumber = randomAccountNumber(12);
        this.amountAvail = 0.0;
    }

    // Getters
    public Integer getRoutingNumber() {
        return routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmountAvail() {
        return amountAvail;
    }

    public void setRoutingNumber(Integer routingNumber) {
        this.routingNumber = routingNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAmountAvail(double amountAvail) {
        this.amountAvail = amountAvail;
    }

    public static int randomRoutingNumber() {
        return random.nextInt(900_000_000) + 100_000_000;
    }

    public static String randomAccountNumber(int length) {
        if (length < 8 || length > 17) {
            throw new IllegalArgumentException("Account number length must be between 8 and 17");
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
