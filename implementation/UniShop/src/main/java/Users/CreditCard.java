package Users;

import java.util.Date;

public class CreditCard {
    // attributes
    private int cardNumber;
    private String firstName;
    private String lastName;
    private Date expiryDate;

    // getters
    public int getCardNumber() { return cardNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Date getExpiryDate() { return expiryDate; }

    // setters
    public void setCardNumber(int cardNumber) { this.cardNumber = cardNumber; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setExpiryDate(Date expiaryDate) { this.expiryDate = expiaryDate; }

    // constructor
    public CreditCard(int cardNumber, String firstName, String lastName, Date expiryDate) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiryDate = expiryDate;
    }
}
