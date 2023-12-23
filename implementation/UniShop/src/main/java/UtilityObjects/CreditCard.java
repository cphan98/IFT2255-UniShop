package UtilityObjects;

/**
 * Class representing a credit card. The credit card includes the card number, first and last name of the owner, and
 * expiry date.
 *
 * This class implements java.io.Serializable.
 */
public class CreditCard implements java.io.Serializable {

    // ATTRIBUTES

    private String cardNumber;
    private String firstName;
    private String lastName;
    private String expiryDate;

    // CONSTRUCTOR

    /**
     * Creates an instance of CreditCard with a given card number, first and last name, and expiry date.
     *
     * @param cardNumber   String, card number
     * @param firstName   String, owner's first name
     * @param lastName   String, owner's last name
     * @param expiryDate  String, expiry date
     */
    public CreditCard(String cardNumber, String firstName, String lastName, String expiryDate) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiryDate = expiryDate;
    }

    // GETTERS

    /**
     * Returns the credit card's number.
     *
     * @return  String, card number
     */
    public String getCardNumber() { return cardNumber; }

    /**
     * Returns the credit card's owner's first name.
     *
     * @return  String, owner's first name
     */
    public String getFirstName() { return firstName; }

    /**
     * Returns the credit card's owner's last name.
     *
     * @return  String, owner's last name
     */
    public String getLastName() { return lastName; }

    /**
     * Returns the credit card's expiry date.
     *
     * @return  String, expiry date
     */
    public String getExpiryDate() { return expiryDate; }

    // SETTERS

    /**
     * Sets the credit card's number.
     *
     * @param cardNumber    String, card number
     */
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    /**
     * Sets the credit card's owner's first name.
     *
     * @param firstName String, owner's first name
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Sets the credit card's owner's last name.
     *
     * @param lastName  String, owner's last name
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Sets the credit card's expiry date.
     *
     * @param expiryDate    String, expiry date
     */
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
