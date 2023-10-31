package Users;

import Metrics.BuyerMetrics;

public class Buyer extends User {
    private String firstName;
    private String lastName;
    private Cart cart;
    private int points;
    private BuyerMetrics metrics;
    public Buyer(String firstName, String lastName, String id, String password, String email, int phoneNumber, String address) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new BuyerMetrics();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new Cart();
        this.points = 0;
    }

}
