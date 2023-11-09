package Users;

import Metrics.BuyerMetrics;
import products.Order;
import products.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class Buyer extends User {
    private String firstName;
    private String lastName;
    private Cart cart;
    private int points;
    private BuyerMetrics metrics;
    private ArrayList<Order> ordersMade;
    private CreditCard card;
    private ArrayList<Product> wishList;
    public Buyer(String firstName, String lastName, String id, String password, String email, String phoneNumber, Address address) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new BuyerMetrics();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new Cart();
        this.points = 0;
        this.wishList = new ArrayList<>();
        this.ordersMade = new ArrayList<>();
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
    public void addOrder(Order order) {
        ordersMade.add(order);
    }
    public HashMap<Seller, HashMap<Product, Integer>> splitCartBeforeOrder() {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = new HashMap<>();
        HashMap<Product, Integer> cartProducts = cart.getProducts();
        for (Product product : cartProducts.keySet()) {
            Seller seller = product.getSeller();
            if (splitCart.containsKey(seller)) {
                HashMap<Product, Integer> sellerProducts = splitCart.get(seller);
                sellerProducts.put(product, cartProducts.get(product));
                splitCart.put(seller, sellerProducts);
            } else {
                HashMap<Product, Integer> sellerProducts = new HashMap<>();
                sellerProducts.put(product, cartProducts.get(product));
                splitCart.put(seller, sellerProducts);
            }
        }
        return splitCart;
    }
    public ArrayList<Order> getOrdersMade() {
        return ordersMade;
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
    public ArrayList<Product> getWishList() {
        return wishList;
    }
    public void addToWishList(Product product) {
        wishList.add(product);
    }

    public String wishListToString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Product product : wishList) {
            sb.append(i).append(". ").append(product.smallToString());
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }
    public String ordersMadeToString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Order order : ordersMade) {
            sb.append(i).append(". ").append(order.toString());
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

}
