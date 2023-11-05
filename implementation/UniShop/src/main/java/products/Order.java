package products;

import Users.Address;
import Users.Buyer;
import Users.CreditCard;
import otherUtility.OrderState;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Order {
    // attributes
    private String id;
    private Buyer buyer;
    private String paymentType;
    private CreditCard paymentInfo;
    private Address shippingAddress;
    private int phoneNumber;
    private String orderDate;
    private HashMap<Product, Integer> products;
    private OrderState status = OrderState.PENDING;
    private String ETA;

    // getters
    public String getId() { return id; }
    public Buyer getBuyer() { return buyer; }
    public String getPaymentType() { return paymentType; }
    public CreditCard getPaymentInfo() { return paymentInfo; }
    public Address getShippingAddress() { return shippingAddress; }
    public int getPhoneNumber() { return phoneNumber; }
    public String getOrderDate() { return orderDate; }
    public HashMap<Product, Integer> getProducts() { return products; }
    public OrderState getStatus() { return status; }
    public String getETA() { return ETA; }

    // setters
    public void setId(String id) { this.id = id; }
    public void setBuyer(Buyer buyer) { this.buyer = buyer; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public void setPaymentInfo(CreditCard paymentInfo) { this.paymentInfo = paymentInfo; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public void setProducts(HashMap<Product, Integer> products) { this.products = products; }
    public void setStatus(OrderState status) { this.status = status; }
    public void setETA(String ETA) { this.ETA = ETA; }

    int idCount = 0;
    private String makeId() {
        int zeros = 3 - Integer.toString(idCount).length();
        return("order" + ("0".repeat(zeros)) + idCount);
    }

    private void setDates() {
        DateTimeFormatter OrderDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.orderDate = OrderDateFormatter.format(LocalDateTime.now());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 7);
        SimpleDateFormat ETAFormatter = new SimpleDateFormat("dd-MM-yyyy");
        this.ETA = ETAFormatter.format(c.toString());
    }

    // constructor with new personal and credit card
    public Order(Buyer buyer, String paymentType, CreditCard paymentInfo, Address shippingAddress, int phoneNumber, HashMap<Product, Integer> products) {
        this.id = makeId();
        this.buyer = buyer;
        this.paymentType = paymentType;
        this.paymentInfo = paymentInfo;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.products = products;
        setDates();
    }

    // constructor with new credit card only
    public Order(Buyer buyer, String paymentType, CreditCard paymentInfo, HashMap<Product, Integer> products) {
        this.id = makeId();
        this.buyer = buyer;
        this.paymentType = paymentType;
        this.paymentInfo = paymentInfo;
        this.shippingAddress = buyer.getAddress();
        this.phoneNumber = buyer.getPhoneNumber();
        this.products = products;
        setDates();
    }

    // constructor with new personal info and points as payment type
    public Order(Buyer buyer, String paymentType, Address shippingAddress, int phoneNumber, HashMap<Product, Integer> products) {
        this.id = makeId();
        this.buyer = buyer;
        this.paymentType = paymentType;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.products = products;
        setDates();
    }

    // constructor with info from profile
    public Order(Buyer buyer, String paymentType, HashMap<Product, Integer> products) {
        this.id = makeId();
        this.buyer = buyer;
        this.paymentType = paymentType;
        if (Objects.equals(paymentType, "credit card")) this.paymentInfo = buyer.getCard();
        this.shippingAddress = buyer.getAddress();
        this.phoneNumber = buyer.getPhoneNumber();
        this.products = products;
        setDates();
    }

    // operations
    public void displayOrder() {
        // TODO
    }

    public void displayProducts() {
        // TODO
    }

    public void confirmReceipt() {
        setStatus(OrderState.DELIVERED);
    }

    public void changeStatus(String status) {
        switch (status) {
            case "accepted" -> setStatus(OrderState.ACCEPTED);
            case "rejected" -> setStatus(OrderState.REJECTED);
            case "in production" -> setStatus(OrderState.INPRODUCTION);
            case "in delivery" -> setStatus(OrderState.INDELIVERY);
        }
    }

    public void cancelOrder() {
        setStatus(OrderState.CANCELLED);
    }

    public void exchangeOrder() {
        // TODO
    }

    public void returnOrder() {
        // TODO
    }

    public void reportIssue() {
        // TODO
    }

    public void sendBuyerNotification() {
        // TODO
    }

    public void sendSellerNotification() {
        // TODO
    }
}
