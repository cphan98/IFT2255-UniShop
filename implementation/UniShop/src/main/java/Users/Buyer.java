package Users;

import Metrics.BuyerMetrics;
import UtilityObjects.Address;
import UtilityObjects.Notification;
import productClasses.Usages.Cart;
import UtilityObjects.CreditCard;
import productClasses.Usages.Evaluation;
import productClasses.Product;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Buyer extends User implements java.io.Serializable {
    // ATTRIBUTES

    private String firstName;
    private String lastName;
    private final Cart cart;
    private int points;
    private final BuyerMetrics metrics;
    private CreditCard card;
    private final ArrayList<Seller> sellersFollowed;
    private final ArrayList<Buyer> buyersFollowed;
    private final ArrayList<Evaluation> evaluationsLiked;
    private final HashMap<Product, Evaluation> evaluationsMade;
    private final ArrayList<Product> wishList;
    private final ArrayList<Buyer> top5;
    private int yourRank;
    public Buyer(String firstName, String lastName, String id, String password, String email, String phoneNumber, Address address) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new BuyerMetrics();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new Cart();
        this.points = 0;
        this.wishList = new ArrayList<>();
        this.sellersFollowed = new ArrayList<>();
        this.buyersFollowed = new ArrayList<>();
        this.top5 = new ArrayList<>();
        this.yourRank = 0;
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
    public int getPoints() {
        return points;
    }
    public ArrayList<Buyer> getTop5ExpBuyers() {
        updateTop5();
        return top5;
    }
    public int getYourRank() {
        updateYourRank();
        return yourRank+1;
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
    public void updateTop5() {
        top5.clear();
        rankBuyers().limit(5).forEach(entry -> {
            top5.add(entry.getKey());
        });
    }
    public void updateYourRank() {
        ArrayList<Buyer> listOfBuyers = new ArrayList<>();
        rankBuyers().forEach(entry ->
                listOfBuyers.add(entry.getKey()));
        if (listOfBuyers.contains(this)) {
            this.yourRank = listOfBuyers.indexOf(this);
        }
    }

    // CONSTRUCTOR

    // OPERATIONS
    @Override
    public void removeFollower(Buyer follower) {
        followers.remove(follower);
        follower.getBuyersFollowed().remove(this);
    }
    public Stream<Map.Entry<Buyer, Integer>> rankBuyers() {
        HashMap<Buyer, Integer> buyersXP = new HashMap<>();
        for (Buyer buyer : getBuyersFollowed()) {
            buyersXP.put(buyer, buyer.getMetrics().getExpPoints());
        }

        Stream<Map.Entry<Buyer, Integer>> sortedStream = buyersXP.entrySet().stream()
                .sorted(Map.Entry.comparingByValue());


        return sortedStream;
    }
    public HashMap<Product, Evaluation> getEvaluationsMade() {
        return evaluationsMade;
    }
    public void toggleBuyerToFollowing(Buyer buyer) {
        if (buyer == this) {
            System.out.println("You cannot follow yourself!");
            return;
        }
        if (!buyersFollowed.contains(buyer)) {
            buyersFollowed.add(buyer);
            buyer.addFollower(this);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() + 1);
            String title = "You have a new follower!";
            String summary = getId() + " is now following you !";
            buyer.addNotification(new Notification(title, summary));
            System.out.println("You are now following " + buyer.getId() + "!");
        } else {
            buyersFollowed.remove(buyer);
            buyer.getFollowers().remove(this);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() - 1);
            System.out.println("You are no longer following " + buyer.getId() + "!");
        }
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
