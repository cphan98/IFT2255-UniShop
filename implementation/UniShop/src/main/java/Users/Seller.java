package Users;

import Metrics.SellerMetrics;
import otherUtility.Category;
import products.Product;

import java.util.ArrayList;

public class Seller extends User {
    private Category category;
    private ArrayList<Product> products;
    private SellerMetrics metrics;
    private int likes;
    public Seller(String id, String password, String email, String phoneNumber, Address address, Category category) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new SellerMetrics();
        this.category = category;
        this.products = new ArrayList<>();
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
    public Category getCategory() {
        return category;
    }
    public SellerMetrics getMetrics() {
        return metrics;
    }
    public void setMetrics(SellerMetrics metrics) {
        this.metrics = metrics;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public int getLikes() {
        return likes;
    }
    public void addLike() {
        likes++;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Seller: ").append(getId()).append("\n");
        sb.append("Category: ").append(getCategory()).append("\n");
        sb.append("Likes: ").append(getLikes()).append("\n");
        sb.append("Products: ").append("\n");
        for (Product product : products) {
            sb.append("\t").append(product.smallToString()).append("\n");
        }
        return sb.toString();
    }

}
