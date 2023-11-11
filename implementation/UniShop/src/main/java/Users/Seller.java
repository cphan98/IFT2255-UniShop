package Users;

import Metrics.SellerMetrics;
import otherUtility.Category;

public class Seller extends User {
    private Category category;
    private SellerMetrics metrics;
    public Seller(String id, String password, String email, int phoneNumber, String address, Category category) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new SellerMetrics();
        this.category = category;
    }

<<<<<<< Updated upstream
=======
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

>>>>>>> Stashed changes
    public Category getCategory() {
        return category;
    }
    public SellerMetrics getMetrics() {
        return metrics;
    }
    public void setMetrics(SellerMetrics metrics) {
        this.metrics = metrics;
    }

}
