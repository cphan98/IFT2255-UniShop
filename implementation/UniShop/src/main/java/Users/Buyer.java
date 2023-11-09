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
    private CreditCard card;
    private ArrayList<Seller> following;
    private ArrayList<Product> wishList;
    public Buyer(String firstName, String lastName, String id, String password, String email, String phoneNumber, Address address) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new BuyerMetrics();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new Cart();
        this.points = 0;
        this.wishList = new ArrayList<>();
        this.following = new ArrayList<>();
    }
    public void addPoints(int points) {
        this.points += points;
    }
    public void addSellerToFollowing(Seller seller) {
        if (!following.contains(seller)) {
            following.add(seller);
        } else {
            System.out.println("You are already following this seller.");
        }
    }
    public void removeSellerFromFollowing(Seller seller) {
        if (following.contains(seller)) {
            following.remove(seller);
        } else {
            System.out.println("You are not following this seller.");
        }
    }
    public void removePoints(int points) {
        this.points -= points;
    }
    public int getPoints() {
        return points;
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
    public void setOrderHistory(ArrayList<Order> ordersMade) {
        this.orderHistory = ordersMade;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            this.firstName = firstName;
        }
    }
    public void setLastName(String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            this.lastName = lastName;
        }
    }
    public ArrayList<Seller> getFollowing() {
        return following;
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

}
