package Users;

import Metrics.BuyerMetrics;
import UtilityObjects.Address;
import productClasses.Usages.Cart;
import UtilityObjects.CreditCard;
import productClasses.Usages.Evaluation;
import productClasses.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class Buyer extends User implements java.io.Serializable {
    // ATTRIBUTES

    private String firstName;
    private String lastName;
    private final Cart cart;
    private final BuyerMetrics metrics;
    private CreditCard card;
    private final ArrayList<Seller> sellersFollowed;
    private final ArrayList<Buyer> buyersFollowed;
    private final ArrayList<Evaluation> evaluationsLiked;
    private final HashMap<Product, Evaluation> evaluationsMade;
    private final ArrayList<Product> wishList;
    public Buyer(String firstName, String lastName, String id, String password, String email, String phoneNumber, Address address) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new BuyerMetrics();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new Cart();
        this.wishList = new ArrayList<>();
        this.sellersFollowed = new ArrayList<>();
        this.buyersFollowed = new ArrayList<>();
        this.evaluationsLiked = new ArrayList<>();
        this.evaluationsMade = new HashMap<>();
    }

    // GETTERS


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public ArrayList<Seller> getSellersFollowed() {
        return sellersFollowed;
    }
    public void addSellersFollowers(Seller seller){
        sellersFollowed.add(seller);
    }
    @Override
    public void removeFollower(Buyer follower) {
        followers.remove(follower);
        follower.getBuyersFollowed().remove(this);
    }

    public Cart getCart() {
        return cart;
    }
    public void addSellersCustomers() {
        for (Product product : getCart().getProducts().keySet()) {
            product.getSeller().addCustomers(this);
        }
    }
    public BuyerMetrics getMetrics() {
        return metrics;
    }
    public CreditCard getCard() {
        return card;
    }
    public ArrayList<Product> getWishList() {
        return wishList;
    }
    public ArrayList<Buyer> getBuyersFollowed() {
        return buyersFollowed;
    }
    public ArrayList<Evaluation> getEvaluationsLiked() {
        return evaluationsLiked;
    }

    // SETTERS

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
    public void setCard(CreditCard card) {
        this.card = card;
    }
      


    // CONSTRUCTOR


    // OPERATIONS


    public HashMap<Product, Evaluation> getEvaluationsMade() {
        return evaluationsMade;
    }

    public String wishListToString() {
        if (wishList.isEmpty()) {
            return "Your wish list is empty!";
        }
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
