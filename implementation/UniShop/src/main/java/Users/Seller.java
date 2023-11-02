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
