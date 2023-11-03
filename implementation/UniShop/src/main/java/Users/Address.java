package Users;

public class Address {
    // attributes
    private String addressLine;
    private String country;
    private String province;
    private String city;
    private String postalCode;

    // getters
    public String getAddressLine() { return addressLine; }
    public String getCountry() { return country; }
    public String getProvince() { return province; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }

    // setters
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }
    public void setCountry(String country) { this.country = country; }
    public void setProvince(String province) { this.province = province; }
    public void setCity(String city) { this.city = city; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}
