package Users;

import Metrics.SellerMetrics;
import UtilityObjects.Address;
import BackEndUtility.Category;
import productClasses.Product;

import java.util.ArrayList;

/**
 * Class containing information about a seller.
 *
 * This class is an extension of User and implements java.io.Serializable.
 */
public class Seller extends User implements java.io.Serializable {

    // ATTRIBUTES

    private Category category;
    private ArrayList<Product> products;
    private ArrayList<Buyer> customers;
    private SellerMetrics metrics;
    private int likes;


    // GETTERS

    /**
     * Returns the category of products the seller is selling.
     *
     * @return  Category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the seller's metrics.
     *
     * @return  SellerMetrics
     */
    public SellerMetrics getMetrics() {
        return metrics;
    }

    /**
     * Returns a list of products the seller is selling.
     *
     * @return  ArrayList of Product
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * Returns the number of likes the seller received.
     *
     * @return  int, number of likes received
     */
    public int getLikes() {
        return likes;
    }

    // SETTERS

    /**
     * Sets the number of likes the seller received.
     *
     * @param likes, int
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    // CONSTRUCTOR

    /**
     * Constructs an instance of Seller with given ID (name), password, email, phone number, address and category of
     * products the seller is selling.
     *
     * @param id            String, seller's name
     * @param password      String
     * @param email         String
     * @param phoneNumber   String
     * @param address       Address
     * @param category      Category
     */
    public Seller(String id, String password, String email, String phoneNumber, Address address, Category category) {
        super(id, password, email, phoneNumber, address);
        this.category = category;
        this.products = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.metrics = new SellerMetrics();
        this.likes = 0;
    }

    // UTILITIES

    // products -------------------------------------------------------------------------------------------------------

    /**
     * Changes the available quantity of a selected product.
     *
     * @param product   Product to edit
     * @param quantity  int, new product quantity
     */
    public void changeProductQuantity(Product product, int quantity) {
        product.setQuantity(quantity);
    }

    /**
     * Removes a product from the seller's list of products they are selling.
     *
     * @param product   Product to remove
     */
    public void removeProduct(Product product) {
        products.remove(product);
    }

    /**
     * Finds a product by title, and returns the product found if it exists.
     *
     * @param title String, title to look up
     * @return      Product with the corresponding title
     */
    public Product findProductByTitle(String title) {
        for (Product p : products) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Updates the revenue and the number of products sold in the seller's metrics
     *
     * @param product   Product sold
     * @param quantity  int, quantity of the product sold
     */
    public void sellProduct(Product product, int quantity) {
        float totalPrice = product.getPrice() * quantity;
        metrics.updateRevenue(metrics.getRevenue() + totalPrice);
        metrics.updateProductsSold(metrics.getProductsSold() + quantity);
    }

    // customer -------------------------------------------------------------------------------------------------------

    /**
     * Returns the seller's list of customers.
     *
     * @return  ArrayList of Buyer
     */
    public ArrayList<Buyer> getCustomers() {
        return customers;
    }

    /**
     * Adds a customer to the seller's list of customers.
     *
     * @param buyer Buyer, customer to add
     */
    public void addCustomers(Buyer buyer) {
        getCustomers().add(buyer);
    }

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Makes a string of the summary of the seller's information, which includes the seller's ID, category of products
     * they are selling, number of likes received by buyers, and the list of products the seller is selling.
     *
     * @return  String, summary of seller's information
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Seller: ").append(getId()).append("\n");
        sb.append("Category: ").append(getCategory()).append("\n");
        sb.append("Likes: ").append(metrics.getLikes()).append("\n");
        sb.append("Products: ").append("\n");
        for (Product product : products) {
            sb.append("\t").append(product.smallToString()).append("\n");
        }
        return sb.toString();
    }

    // print seller's products

    /**
     * Returns a string of all the products the seller is selling. For each product, the title, quantity and price of
     * the product is included.
     *
     * @return  String containing products the seller is selling
     */
    public String productsToString() {
        StringBuilder sb = new StringBuilder();
        products.forEach(product -> sb
                .append("\t")
                .append(product.getTitle())
                .append(" - Quantity: ")
                .append(product.getQuantity())
                .append(", Price: ")
                .append(product.getPrice())
                .append("$")
                .append("\n"));
        return sb.toString();
    }
}
