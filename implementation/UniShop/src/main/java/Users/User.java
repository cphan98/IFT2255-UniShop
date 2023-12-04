package Users;

import UtilityObjects.Address;
import UtilityObjects.Notification;
import productClasses.Usages.Order;

import java.util.*;

public abstract class User implements java.io.Serializable {
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

    //getters and setters

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
    public User getFollower(String id) {
        for (User follower : getFollowers()) {
            if (follower.getId().equals(id)) {
                return follower;
            }
        }
        return null;
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
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) { this.address = address; }
    public Queue<Notification> getNotifications() {
        return notifications;
    }
    public void setNotifications(Queue<Notification> notifications) {
        this.notifications = notifications;
    }
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }
    public void setOrderHistory(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }
    public void addOrder(Order order) {
        orderHistory.add(order);
    }
    public Date getStartTime(){
        return startTime;
    }
    public boolean getChecked24H(){
        return checked24H;
    }
    public void setChecked24H(boolean check24H){
        this.checked24H = check24H;
    }
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

    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'';
    }
}
