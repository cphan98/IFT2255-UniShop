package Users;

import Metrics.BuyerMetrics;

public class Buyer extends User {
    private String firstName;
    private String lastName;
    private Cart cart;
    private int points;
    private BuyerMetrics metrics;
    private CreditCard card;
    public Buyer(String firstName, String lastName, String id, String password, String email, int phoneNumber, Address address) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new BuyerMetrics();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new Cart();
        this.points = 0;
    }
    public void addPoints(int points) {
        this.points += points;
    }
    public void removePoints(int points) {
        this.points -= points;
    }
    public int getPoints() {
        return points;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Cart getCart() {
        return cart;
    }
    public BuyerMetrics getMetrics() {
        return metrics;
    }
    public CreditCard getCard() {
        return card;
    }
    public void setCard(CreditCard card) {
        this.card = card;
    }
}
