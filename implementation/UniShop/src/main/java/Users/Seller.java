package Users;

import Metrics.SellerMetrics;
import UtilityObjects.Address;
import BackEndUtility.Category;
import productClasses.Product;

import java.util.ArrayList;

public class Seller extends User {

    private Category category;
    private ArrayList<Product> products;
    private ArrayList<Buyer> customers;
    private SellerMetrics metrics;
    private int likes;


    public Seller(String id, String password, String email, String phoneNumber, Address address, Category category) {
        super(id, password, email, phoneNumber, address);
        this.category = category;
        this.products = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.metrics = new SellerMetrics();
        this.likes = 0;

    }

    public void changeProductQuantity(Product product, int quantity) {
        product.setQuantity(quantity);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    // Use this method to find a product by title
    public Product findProductByTitle(String title) {
        for (Product p : products) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }
        return null;
    }

    public void sellProduct(Product product, int quantity) {
        float totalPrice = product.getPrice() * quantity;
        metrics.updateRevenue(metrics.getRevenue() + totalPrice);
        metrics.updateProductsSold(metrics.getProductsSold() + quantity);
    }

    public Category getCategory() {
        return category;
    }
    public SellerMetrics getMetrics() {
        return metrics;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public ArrayList<Buyer> getCustomers() {
        return customers;
    }
    public void addCustomers(Buyer buyer) {
        getCustomers().add(buyer);
    }
    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
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

}
