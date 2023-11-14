package UtilityObjects;

import java.util.Date;

public class CreditCard {
    // attributes
    private String cardNumber;
    private String firstName;
    private String lastName;
    private String expiryDate;

    // getters
    public String getCardNumber() { return cardNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getExpiryDate() { return expiryDate; }

    // setters
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    // constructor
    public CreditCard(String cardNumber, String firstName, String lastName, String expiryDate) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiryDate = expiryDate;
    }
}
