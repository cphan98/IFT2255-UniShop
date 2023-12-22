package Users;

import Metrics.BuyerMetrics;
import UtilityObjects.Address;
import productClasses.Usages.Cart;
import UtilityObjects.CreditCard;
import productClasses.Usages.Evaluation;
import productClasses.Product;

import java.util.*;
import java.util.stream.Stream;

/**
 * Class containing information about a buyer.
 *
 * This class is an extension of User and implements java.io.Serializable.
 */
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
    private final ArrayList<Buyer> top5;
    private int yourRank;

    // CONSTRUCTOR

    /**
     * Constructs a new instance of Buyer with given first and last name, ID (username), password, email, phone number
     * and address.
     *
     * @param firstName     String
     * @param lastName      String
     * @param id            String, username
     * @param password      String
     * @param email         String
     * @param phoneNumber   String
     * @param address       Address
     */
    public Buyer(String firstName, String lastName, String id, String password, String email, String phoneNumber, Address address) {
        super(id, password, email, phoneNumber, address);
        this.metrics = new BuyerMetrics();
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new Cart();
        this.wishList = new ArrayList<>();
        this.sellersFollowed = new ArrayList<>();
        this.buyersFollowed = new ArrayList<>();
        this.top5 = new ArrayList<>();
        this.yourRank = 0;
        this.evaluationsLiked = new ArrayList<>();
        this.evaluationsMade = new HashMap<>();
    }

    // GETTERS

    /**
     * Returns the buyer's first name.
     *
     * @return  String, first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the buyer's last name.
     *
     * @return  String, last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the list of sellers the buyer is following.
     *
     * @return  ArrayList of Seller
     */
    public ArrayList<Seller> getSellersFollowed() {
        return sellersFollowed;
    }

    /**
     * Returns the top 5 buyers the buyer is following with the highest experience points.
     *
     * @return  ArrayList of Buyer
     */
    public ArrayList<Buyer> getTop5ExpBuyers() {
        updateTop5();
        return top5;
    }

    /**
     * Returns the buyer's rank in experience points within the buyers they are following.
     *
     * @return  int
     */
    public int getYourRank() {
        updateYourRank();
        return yourRank+1;
    }


    /**
     * Returns the buyer's cart.
     *
     * @return  Cart
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Returns the buyer's metrics.
     *
     * @return  BuyerMetrics
     */
    public BuyerMetrics getMetrics() {
        return metrics;
    }

    /**
     * Returns the buyer's credit card.
     *
     * @return  CreditCard
     */
    public CreditCard getCard() {
        return card;
    }

    /**
     * Returns the buyer's wishlist.
     *
     * @return  ArrayList of Product
     */
    public ArrayList<Product> getWishList() {
        return wishList;
    }

    /**
     * Returns the list of buyers the buyer is following.
     *
     * @return  ArrayList of Buyer
     */
    public ArrayList<Buyer> getBuyersFollowed() {
        return buyersFollowed;
    }

    /**
     * Returns the list of evaluations the buyer liked.
     *
     * @return  ArrayList of Evaluation
     */
    public ArrayList<Evaluation> getEvaluationsLiked() {
        return evaluationsLiked;
    }

    /**
     * Returns the list of evaluation the buyer made.
     *
     * @return  HashMap: Product as key, Evaluation as value
     */
    public HashMap<Product, Evaluation> getEvaluationsMade() {
        return evaluationsMade;
    }

    // SETTERS

    /**
     * Sets the first name of the buyer.
     *
     * @param firstName String
     */
    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) {
            this.firstName = firstName;
        }
    }

    /**
     * Sets the last name of the buyer.
     *
     * @param lastName  String
     */
    public void setLastName(String lastName) {
        if (lastName != null && !lastName.isEmpty()) {
            this.lastName = lastName;
        }
    }

    /**
     * Sets the buyer's credit card.
     *
     * @param card  CreditCard
     */
    public void setCard(CreditCard card) {
        this.card = card;
    }

    // UTILITIES

    // followers ------------------------------------------------------------------------------------------------------

    /**
     * Removes a follower from the buyer's list of followers.
     */
    @Override
    public void removeFollower(Buyer follower) {
        followers.remove(follower);
        follower.getBuyersFollowed().remove(this);
    }

    private void updateTop5() {
        top5.clear();
        rankBuyers().limit(5).forEach(entry -> {
            top5.add(entry.getKey());
        });
    }

    private void updateYourRank() {
        ArrayList<Buyer> listOfBuyers = new ArrayList<>();
        rankBuyers().forEach(entry ->
                listOfBuyers.add(entry.getKey()));
        if (listOfBuyers.contains(this)) {
            this.yourRank = listOfBuyers.indexOf(this);
        }
    }

    private Stream<Map.Entry<Buyer, Integer>> rankBuyers() {
        HashMap<Buyer, Integer> buyersXP = new HashMap<>();
        buyersXP.put(this, this.getMetrics().getExpPoints());
        for (Buyer buyer : getBuyersFollowed()) {
            buyersXP.put(buyer, buyer.getMetrics().getExpPoints());
        }

        Stream<Map.Entry<Buyer, Integer>> sortedStream = buyersXP.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        return sortedStream;
    }


    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Transforms the buyer's wishlist into a string in order to properly print it.
     *
     * @return  String containing the products in the buyer's wishlist
     */
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