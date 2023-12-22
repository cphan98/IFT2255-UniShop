package Users;

import UtilityObjects.Address;
import UtilityObjects.Notification;
import productClasses.Usages.Order;

import java.util.*;

/**
 * Abstract class representing a user. A user can be a Buyer or a Seller.
 *
 * This class implements java.io.Serializable.
 */
public abstract class User implements java.io.Serializable {

    // ATTRIBUTES

    protected String id;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected Address address;
    protected ArrayList<Buyer> followers;
    protected Queue<Notification> notifications;
    protected ArrayList<Order> orderHistory;
    protected Date startTime;
    protected boolean checked24H;

    // CONSTRUCTOR

    /**
     * Constructs an instance of User with given unique ID, password, email, phone number and address.
     *
     * @param id            String, unique
     * @param password      String
     * @param email         String
     * @param phoneNumber   String
     * @param address       Address
     */
    public User(String id, String password, String email, String phoneNumber, Address address) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderHistory = new ArrayList<>();
        this.startTime = Calendar.getInstance().getTime();
        this.checked24H = false;
        this.followers = new ArrayList<>();
        this.notifications = new LinkedList<>();
    }

    // GETTERS

    /**
     * Returns the user's ID.
     *
     * @return  String, user's id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the user's password.
     *
     * @return  String, user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the list of the user's followers.
     *
     * @return  ArrayList of Buyer who follows the user
     */
    public ArrayList<Buyer> getFollowers() {
        return followers;
    }

    /**
     * Searches for a follower by ID and returns the follower found if present.
     *
     * @param id    String, follower's ID
     * @return      Buyer, follower of user
     */
    public User getFollower(String id) {
        for (User follower : getFollowers()) {
            if (follower.getId().equals(id)) {
                return follower;
            }
        }
        return null;
    }

    /**
     * Returns the user's email.
     *
     * @return  String, user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's phone number.
     *
     * @return  String, user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the user's address
     *
     * @return  Address, user's address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns a list of notifications the user received.
     *
     * @return  Queue of Notification
     */
    public Queue<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Returns a list of orders a user received/made.
     *
     * @return  ArrayList of Order
     */
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    /**
     * Returns the date the user signed up.
     *
     * @return  Date, user's sign up date
     */
    public Date getStartTime(){
        return startTime;
    }

    /**
     * Returns true if the user signed in within 24h of their sign-up.
     *
     * @return  Boolean, true if the user signed in within 24h of their sign-up
     */
    public boolean getChecked24H(){
        return checked24H;
    }

    // SETTERS

    /**
     * Sets the user's id.
     *
     * @param id    String, unique ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the user's password.
     *
     * @param password  String, user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user's list of followers.
     *
     * @param followers ArrayList of Buyer, user's followers
     */
    public void setFollowers(ArrayList<Buyer> followers) {
        this.followers = followers;
    }

    /**
     * Sets the user's email.
     *
     * @param email String, user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phoneNumber   String, user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the user's address.
     *
     * @param address   Address, user's address
     */
    public void setAddress(Address address) { this.address = address; }

    /**
     * Sets the user's list of notifications.
     *
     * @param notifications ArrayList of Notification, user's notifications
     */
    public void setNotifications(Queue<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * Sets the user's order history.
     *
     * @param orderHistory  ArrayList of Order, user's orders
     */
    public void setOrderHistory(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    /**
     * Sets the 24h check status. The status is set to true if the user signed in within 24h of their sign-up.
     *
     * @param check24H  Boolean, 24h check status
     */
    public void setChecked24H(boolean check24H){
        this.checked24H = check24H;
    }

    // UTILITIES

    // orders ---------------------------------------------------------------------------------------------------------

    /**
     * Adds an order in the user's order history.
     *
     * @param order Order
     */
    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    // notifications --------------------------------------------------------------------------------------------------

    /**
     * Adds a notification to the user's list of notifications.
     *
     * @param notification  Notification, notification to be added
     */
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    // follower -------------------------------------------------------------------------------------------------------

    /**
     * Adds a follower to the user's list of followers.
     *
     * @param buyer Buyer, follower to be added
     */
    public void addFollower(Buyer buyer) {
        followers.add(buyer);
    }

    /**
     * Removes a follower from the user's list of followers.
     *
     * @param buyer Buyer, follower to remove
     */
    public void removeFollower(Buyer buyer) {
        followers.remove(buyer);
    }

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Returns a string containing the user's notifications.
     *
     * @return  String, notifications received
     */
    public String notificationsToString() {
        if (notifications.isEmpty()) {
            return "You have no notifications!";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Notification notification : notifications) {
            sb.append(i).append(". ").append(notification.toString());
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

    /**
     * Returns a string containing the orders in the user's order history.
     *
     * @return  String, orders from user's order history
     */
    public String ordersMadeToString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Order order : orderHistory) {
            sb.append(i).append(". ").append(order.smallToString());
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

    /**
     * Returns a string of the user's basic information, such as ID, password, email, phone number and address.
     * This is used to make an instance of User more readable.
     *
     * @return  String, user's basic information: ID, password, email, phone number, address
     */
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'';
    }
}
