package Users;

import java.util.ArrayList;

public abstract class User {
    private String id;
    private String password;
    private String email;
    private int phoneNumber;
    private String address;
    private ArrayList<Buyer> followers;

    //getters and setters

    public User(String id, String password, String email, int phoneNumber, String address) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<Buyer> getFollowers() {
        return followers;
    }
    public void setFollowers(ArrayList<Buyer> followers) {
        this.followers = followers;
    }
    public void addFollower(Buyer buyer) {
        followers.add(buyer);
    }
    public void removeFollower(Buyer buyer) {
        followers.remove(buyer);
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) { this.address = address; }
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'';
    }
}
