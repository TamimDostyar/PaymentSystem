package payment.accounts;
import java.util.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;



public class Transaction {
    
    private String description;
    private Integer transAmount;
    private String date;
    private String type;
    private String toAccountNumber;
    private Integer toRoutingNumber;



    Transaction(){
        this.date = getCurrentDate();
    }

    public Transaction(String description, Integer transamount, String date, String type){
        this.description = description;
        this.transAmount = transamount;
        this.date = date;
        this.type = type;
    }

    public Transaction(String description, Integer transamount, String type, 
                       String toAccountNumber, Integer toRoutingNumber){
        this.description = description;
        this.transAmount = transamount;
        this.date = getCurrentDate();
        this.type = type;
        this.toAccountNumber = toAccountNumber;
        this.toRoutingNumber = toRoutingNumber;
    }




    public Integer getTransAmount(){return this.transAmount;}
    public String getDescription(){return this.description;}
    public String getDate(){return this.date;}
    public String getType(){return this.type;}
    public String getToAccountNumber(){return this.toAccountNumber;}
    public Integer getToRoutingNumber(){return this.toRoutingNumber;}



    public static String getCurrentDate(){
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;

    }
}
