package productClasses.Usages;

import Users.*;
import UtilityObjects.Address;
import UtilityObjects.CreditCard;
import BackEndUtility.OrderState;
import productClasses.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class that represents an order, which is a request from a buyer to the seller to buy a number of products
 */
public class Order implements java.io.Serializable {

    // ATTRIBUTES

    private String id;
    private Buyer buyer;
    private String paymentType;
    private CreditCard paymentInfo;
    private Address shippingAddress;
    private String phoneNumber;
    private final LocalDate today = LocalDate.now();
    private String orderDate = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private HashMap<Product, Integer> products;
    private OrderState status = OrderState.IN_PRODUCTION;
    private String ETA;
    private IssueQuery issue;
    private String shippingCompany;
    private String shippingNumber;
    private float totalCost;

    // CONSTRUCTORS

    /**
     * Method to make an id for the order
     *
     * @param idCount the number of orders that have been made so far
     * @return the id of the order
     */
    public String makeId(int idCount) {
        int zeros = 3 - Integer.toString(idCount).length();
        return("order" + ("0".repeat(zeros)) + idCount);
    }

    /**
     * Method that returns the total price of the order
     *
     * @return the total price of the order
     */
    public float getTotalPrice() {
        float total = products.entrySet().stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(0f, Float::sum);
        return Math.round(total * 100.0) / 100.0f;
    }


    /**
     * Constructor of the class Order with new personal info and credit card
     *
     * @param buyer the buyer that made the order
     * @param paymentType the type of payment
     * @param paymentInfo the credit card info
     * @param shippingAddress the shipping address
     * @param phoneNumber the phone number
     * @param products the products to buy
     */
    public Order(Buyer buyer,
                 String paymentType,
                 CreditCard paymentInfo,
                 Address shippingAddress,
                 String phoneNumber,
                 HashMap<Product, Integer> products) {
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
        this.totalCost = getTotalPrice();
    }

    /**
     * Constructor of the class Order with new credit card only
     *
     * @param buyer the buyer that made the order
     * @param paymentType the type of payment
     * @param paymentInfo the credit card info
     * @param products the products to buy
     */
    public Order(Buyer buyer,
                 String paymentType,
                 CreditCard paymentInfo,
                 HashMap<Product, Integer> products) {
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
        this.totalCost = getTotalPrice();
    }

    /**
     * Constructor of the class Order with new personal info and points as payment type
     *
     * @param buyer the buyer that made the order
     * @param paymentType the type of payment
     * @param shippingAddress the shipping address
     * @param phoneNumber the phone number
     * @param products the products to buy
     */
    public Order(Buyer buyer,
                 String paymentType,
                 Address shippingAddress,
                 String phoneNumber,
                 HashMap<Product, Integer> products) {
        this.buyer = buyer;
        this.paymentType = paymentType;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.products = new HashMap<>();
        this.products.putAll(products);
        this.ETA = makeRandomETA();
        this.totalCost = getTotalPrice();
    }


    /**
     * Constructor of the class Order with info from the buyer's profile
     *
     * @param buyer the buyer that made the order
     * @param paymentType the type of payment
     * @param products the products to buy
     */
    public Order(Buyer buyer,
                 String paymentType,
                 HashMap<Product, Integer> products) {
        this.buyer = buyer;
        this.paymentType = paymentType;
        if (Objects.equals(paymentType, "credit card")) {
            this.paymentInfo = buyer.getCard();
        } else {
            this.paymentInfo = new CreditCard("00000000", "Used", "Points", "0000-00-00");
        }
        this.shippingAddress = buyer.getAddress();
        this.phoneNumber = buyer.getPhoneNumber();
        this.products = new HashMap<>();
        this.products.putAll(products);
        this.ETA = makeRandomETA();
        this.totalCost = getTotalPrice();
    }

    // GETTERS

    /**
     * Method that returns the id of the order
     *
     * @return the id of the order
     */
    public String getId() { return id; }

    /**
     * Method that returns the buyer that made the order
     * @return the buyer that made the order
     */
    public Buyer getBuyer() { return buyer; }

    /**
     * Method that returns the type of payment
     * @return the type of payment, either "credit card" or "points"
     */
    public String getPaymentType() { return paymentType; }

    /**
     * Method that returns the credit card info
     * @return the credit card
     */
    public CreditCard getPaymentInfo() { return paymentInfo; }

    /**
     * Method that returns the shipping address
     * @return the shipping address
     */
    public Address getShippingAddress() { return shippingAddress; }

    /**
     * Method that returns the phone number
     * @return the phone number
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Method that returns the date of the order
     * @return the date of the order
     */
    public String getOrderDate() { return orderDate; }

    /**
     * Method that returns the products to buy
     * @return the products to buy
     */
    public HashMap<Product, Integer> getProducts() { return products; }

    /**
     * Method that returns the status of the order
     * @return the status of the order, among the values of the enum OrderState
     */
    public OrderState getStatus() { return status; }

    /**
     * Method that returns the ETA of the order
     * @return the ETA of the order
     */
    public String getETA() { return ETA; }

    /**
     * Method that returns the issue of the order
     * @return the issue of the order
     */
    public IssueQuery getIssue() { return issue; }

    /**
     * Method that returns the shipping company
     * @return the name of the shipping company
     */
    public String getShippingCompany() { return shippingCompany; }

    /**
     * Method that returns the shipping number
     * @return the shipping number
     */
    public String getShippingNumber() { return shippingNumber; }

    /**
     * Method that returns the total cost of the order
     * @return the total cost of the order
     */
    public float getTotalCost() { return totalCost; }

    // SETTERS

    /**
     * Method that sets the id of the order
     * @param id the new id of the order
     */
    public void setId(String id) { this.id = id; }

    /**
     * Method that sets the buyer that made the order
     * @param buyer the new buyer that made the order
     */
    public void setBuyer(Buyer buyer) { this.buyer = buyer; }

    /**
     * Method that sets the type of payment
     * @param paymentType the new type of payment
     */
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    /**
     * Method that sets the credit card info
     * @param paymentInfo the new credit card info
     */
    public void setPaymentInfo(CreditCard paymentInfo) { this.paymentInfo = paymentInfo; }

    /**
     * Method that sets the shipping address
     * @param shippingAddress the new shipping address
     */
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }

    /**
     * Method that sets the phone number
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Method that sets the date of the order
     * @param orderDate the new date of the order
     */
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    /**
     * Method that sets the products to buy
     * @param products the new products to buy
     */
    public void setProducts(HashMap<Product, Integer> products) { this.products = products; }

    /**
     * Method that sets the status of the order
     * @param status the new status of the order
     */
    public void setStatus(OrderState status) { this.status = status; }

    /**
     * Method that sets the ETA of the order
     * @param ETA the new ETA of the order
     */
    public void setETA(String ETA) { this.ETA = ETA; }

    /**
     * Method that sets the issue of the order
     * @param issue the new issue of the order
     */
    public void setIssue(IssueQuery issue) { this.issue = issue; }

    /**
     * Method that sets the shipping company
     * @param company the new shipping company
     */
    public void setShippingCompany(String company) {this.shippingCompany = company; }

    /**
     * Method that sets the shipping number
     * @param number the new shipping number
     */
    public void setShippingNumber(String number) { this.shippingNumber = number; }

    /**
     * Method that sets the total cost of the order
     * @param cost
     */
    public void setTotalCost(float cost) { this.totalCost = cost; }

    // UTILITIES

    // eta ------------------------------------------------------------------------------------------------------------

    private String makeRandomETA() {
        int day = (int) (Math.random() * 30);
        return "Within " + day + " days";
    }

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Method that returns the string representation of the products to buy
     * @return the string representation of the products to buy
     */
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

    /**
     * Method that returns the string representation of the order
     * @return the string representation of the order
     */
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

    /**
     * Method that returns a small string representation of the order to make it easier to read
     * @return the small string representation of the order
     */
    public String smallToString() {
        return "Order ID: " + id + "\n" + productsToString() + "Status: " + status + "\n";
    }
}
