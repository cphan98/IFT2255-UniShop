package Users;

import Metrics.SellerMetrics;

public class Seller extends User {
    private String category;
    private SellerMetrics metrics;
    public Seller(String id, String password, String email, int phoneNumber, String address, String category) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new SellerMetrics();
        this.category = category;
    }
}
