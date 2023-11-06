package Users;

import Metrics.SellerMetrics;
import otherUtility.Category;
import products.Product;

import java.util.ArrayList;

public class Seller extends User {
    private Category category;
    private ArrayList<Product> products;
    private SellerMetrics metrics;
    public Seller(String id, String password, String email, String phoneNumber, Address address, Category category) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new SellerMetrics();
        this.category = category;
        this.products = new ArrayList<>();
    }
    public void addProduct(Product product) {
        // Check if the product already exists
        Product existingProduct = findProductByTitle(product.getTitle());
        if (existingProduct != null) {
            // If exists, increase the quantity
            existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
        } else {
            // If not, add as a new product
            products.add(product);
        }
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    // Use this method to find a product by title
    private Product findProductByTitle(String title) {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Seller: ").append(getId()).append("\n");
        sb.append("Category: ").append(getCategory()).append("\n");
        sb.append("Products: ").append("\n");
        for (Product product : products) {
            sb.append(product.smallToString()).append("\n");
        }
        return sb.toString();
    }

}
