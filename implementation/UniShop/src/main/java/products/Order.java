package products;

import Users.*;
import otherUtility.OrderState;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Order {
    // ATTRIBUTES

    private String id;
    private Buyer buyer;
    private String paymentType;
    private CreditCard paymentInfo;
    private Address shippingAddress;
    private String phoneNumber;
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private String orderDate = today.format(formatter);
    private HashMap<Product, Integer> products;
    private OrderState status = OrderState.INPRODUCTION;
    private String ETA;

    // GETTERS

    public String getId() { return id; }
    public Buyer getBuyer() { return buyer; }
    public String getPaymentType() { return paymentType; }
    public CreditCard getPaymentInfo() { return paymentInfo; }
    public Address getShippingAddress() { return shippingAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getOrderDate() { return orderDate; }
    public HashMap<Product, Integer> getProducts() { return products; }
    public OrderState getStatus() { return status; }
    public String getETA() { return ETA; }
    public HashMap<Product, Integer> getProductsList() {
        return products;
    }

    // SETTERS

    public void setId(String id) { this.id = id; }
    public void setBuyer(Buyer buyer) { this.buyer = buyer; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public void setPaymentInfo(CreditCard paymentInfo) { this.paymentInfo = paymentInfo; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public void setProducts(HashMap<Product, Integer> products) { this.products = products; }
    public void setStatus(OrderState status) { this.status = status; }
    public void setETA(String ETA) { this.ETA = ETA; }
    public String makeId(int idCount) {
        int zeros = 3 - Integer.toString(idCount).length();
        return("order" + ("0".repeat(zeros)) + idCount);
    }

    // CONSTRUCTORS

    // constructor with new personal and credit card
    public Order(Buyer buyer, String paymentType, CreditCard paymentInfo, Address shippingAddress, String phoneNumber, HashMap<Product, Integer> products) {
        this.buyer = buyer;
        this.paymentType = paymentType;
        this.paymentInfo = new CreditCard(
                paymentInfo.getCardNumber(),
                paymentInfo.getFirstName(),
                paymentInfo.getLastName(),
                paymentInfo.getExpiryDate()
        );
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.products = new HashMap<>();
        this.products.putAll(products);
        this.ETA = makeRandomETA();
    }

    // constructor with new credit card only
    public Order(Buyer buyer, String paymentType, CreditCard paymentInfo, HashMap<Product, Integer> products) {
        this.buyer = buyer;
        this.paymentType = paymentType;
        this.paymentInfo = new CreditCard(
                paymentInfo.getCardNumber(),
                paymentInfo.getFirstName(),
                paymentInfo.getLastName(),
                paymentInfo.getExpiryDate()
        );
        this.shippingAddress = buyer.getAddress();
        this.phoneNumber = buyer.getPhoneNumber();
        this.products = new HashMap<>();
        this.products.putAll(products);
        this.ETA = makeRandomETA();

    }

    // constructor with new personal info and points as payment type
    public Order(Buyer buyer, String paymentType, Address shippingAddress, String phoneNumber, HashMap<Product, Integer> products) {
        this.buyer = buyer;
        this.paymentType = paymentType;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.products = new HashMap<>();
        this.products.putAll(products);
        this.ETA = makeRandomETA();
    }

    // constructor with info from profile
    public Order(Buyer buyer, String paymentType, HashMap<Product, Integer> products) {
        this.buyer = buyer;
        this.paymentType = paymentType;
        if (Objects.equals(paymentType, "credit card")) this.paymentInfo = buyer.getCard();
        this.shippingAddress = buyer.getAddress();
        this.phoneNumber = buyer.getPhoneNumber();
        this.products = new HashMap<>();
        this.products.putAll(products);
        this.ETA = makeRandomETA();
    }

    // FUNCTIONS & METHODS

    // ORDER STATUS

    public void changeStatus(OrderState status) {
        switch (status) {
            case PENDING -> setStatus(OrderState.PENDING);
            case ACCEPTED -> setStatus(OrderState.ACCEPTED);
            case REJECTED -> setStatus(OrderState.REJECTED);
            case INPRODUCTION -> setStatus(OrderState.INPRODUCTION);
            case INDELIVERY -> setStatus(OrderState.INDELIVERY);
            case DELIVERED -> setStatus(OrderState.DELIVERED);
        }
    }

    public void cancelOrder() {
        setStatus(OrderState.CANCELLED);
        buyer.getMetrics().setOrdersMade(buyer.getMetrics().getOrdersMade() - 1);
    }

    // NOTIFICATIONS

    public void sendBuyerNotification(Buyer buyer, String title, String summary) {
        buyer.addNotification(new Notification(title, summary));
    }

    public void sendSellerNotification(Seller seller, String title, String summary) {
        seller.addNotification(new Notification(title, summary));
    }

    public String productsToString() {
        StringBuilder sb = new StringBuilder();
        products.forEach((product, quantity) -> sb.append("\t").append(product.getTitle())
                .append(" - Quantity: ")
                .append(quantity)
                .append(", Price: ")
                .append(product.getPrice()).append("$")
                .append("\n"));
        return sb.toString();
    }

    public String toString() {
        return "Order ID: " + id + "\n" +
                "Buyer: " + buyer.getId() + "\n" +
                "Payment Type: " + paymentType + "\n" +
                "Payment Info: " + paymentInfo.getCardNumber() + "\n" +
                "Shipping Address: " + shippingAddress.toString() + "\n" +
                "Phone Number: " + phoneNumber + "\n" +
                "Order Date: " + orderDate + "\n" +
                "Products: " + "\n" +
                productsToString() +
                "Total Price: " + getTotalPrice() + "$" + "\n" +
                "Status: " + status + "\n" +
                "ETA: " + ETA + "\n";
    }
    public String smallToString() {
        return "Order ID: " + id + "\n" + productsToString() + "Status: " + status + "\n";
    }

    public float getTotalPrice() {
        return products.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0f, Float::sum);
    }

    public String makeRandomETA() {
        int day = (int) (Math.random() * 30);
        return "Within " + day + " days";
    }
}
