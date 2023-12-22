package UtilityObjects;

/**
 * Class representing an address. The Canadian address format is used.
 *
 * The address includes the address line, country, province, city and postal code.
 *
 * This class implements java.io.Serializable.
 */
public class Address implements java.io.Serializable {

    // ATTRIBUTES

    private String addressLine;
    private String country;
    private String province;
    private String city;
    private String postalCode;

    // CONSTRUCTOR

    /**
     * Constructs an instance of Address with given street address, country, province, city and postal code.
     *
     * @param addressLine   String, street address
     * @param country       String, country
     * @param province      String, province
     * @param city          String, city
     * @param postalCode    String, postal code
     */
    public Address(String addressLine, String country, String province, String city, String postalCode) {
        this.addressLine = addressLine;
        this.country = country;
        this.province = province;
        this.city = city;
        this.postalCode = postalCode;
    }

    // GETTERS

    /**
     * Returns the street address.
     *
     * @return  String, street address
     */
    public String getAddressLine() { return addressLine; }

    /**
     * Returns the country.
     *
     * @return  String, country
     */
    public String getCountry() { return country; }

    /**
     * Returns the province.
     *
     * @return  String, province
     */
    public String getProvince() { return province; }

    /**
     * Returns the city.
     *
     * @return  String, city
     */
    public String getCity() { return city; }

    /**
     * Returns the postal code.
     *
     * @return  String, postal code
     */
    public String getPostalCode() { return postalCode; }

    // SETTERS

    /**
     * Sets the street address.
     *
     * @param addressLine   String, street address
     */
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    /**
     * Sets the country.
     *
     * @param country   String, country
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * Sets the province.
     *
     * @param province  String, province
     */
    public void setProvince(String province) { this.province = province; }

    /**
     * Sets the city.
     *
     * @param city  String, city
     */
    public void setCity(String city) { this.city = city; }

    /**
     * Sets the postal code.
     *
     * @param postalCode    String, postal code
     */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    // UTILITIES

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Returns a string of the address.
     *
     * @return  String, address information, including address line, city, province, country and postal code
     */
    public String toString() {
        return addressLine + ", " + city + ", " + province + ", " + country + ", " + postalCode;
    }
}
