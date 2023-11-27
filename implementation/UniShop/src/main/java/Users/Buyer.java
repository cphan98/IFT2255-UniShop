package Users;

import Metrics.BuyerMetrics;
import UtilityObjects.Address;
import productClasses.Usages.Cart;
import UtilityObjects.CreditCard;
import UtilityObjects.Notification;
import productClasses.Usages.Evaluation;
import productClasses.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class Buyer extends User implements java.io.Serializable {
    private String firstName;
    private String lastName;
    private final Cart cart;
    private int points;
    private final BuyerMetrics metrics;
    private CreditCard card;
    private final ArrayList<Seller> sellersFollowed;
    private final ArrayList<Buyer> buyersFollowed;
    private final ArrayList<Evaluation> evaluationsLiked;
    private final ArrayList<Product> wishList;
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
        this.evaluationsLiked = new ArrayList<>();
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
    public HashMap<Seller, HashMap<Product, Integer>> splitCartBeforeOrder() {
        HashMap<Seller, HashMap<Product, Integer>> splitCart = new HashMap<>();
        HashMap<Product, Integer> cartProducts = cart.getProducts();
        for (Product product : cart.getProducts().keySet()) {
            addPoints(product.getBasePoints()*cart.getProducts().get(product));
            System.out.println("With this purchase, you won " + product.getBasePoints()*cart.getProducts().get(product) + " buying points!\n");
        }
        for (Product product : cartProducts.keySet()) {
            Seller seller = product.getSeller();
            HashMap<Product, Integer> sellerProducts;
            if (splitCart.containsKey(seller)) {
                sellerProducts = splitCart.get(seller);
            } else {
                sellerProducts = new HashMap<>();
            }
            sellerProducts.put(product, cartProducts.get(product));
            splitCart.put(seller, sellerProducts);

        }
        for ( Seller seller : splitCart.keySet())
        {
            for (Product product : splitCart.get(seller).keySet())
            {
                String title = "New order!";
                String summary = this.getId() + " just bought your " + product.getTitle() + "!";
                seller.addNotification(new Notification(title, summary));
            }
        }
        return splitCart;
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
    public ArrayList<Seller> getSellersFollowed() {
        return sellersFollowed;
    }
    public void addSellersFollowers(Seller seller){
        sellersFollowed.add(seller);
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
    public ArrayList<Buyer> getBuyersFollowed() {
        return buyersFollowed;
    }
    public ArrayList<Evaluation> getEvaluationsLiked() {
        return evaluationsLiked;
    }
    public void toggleProductToWishList(Product product) {
        if (wishList.contains(product)) {
            wishList.remove(product);
            product.setLikes(product.getLikes() - 1);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() - 1);
            System.out.println("Product removed from wish list!");
        } else {
            wishList.add(product);
            product.setLikes(product.getLikes() + 1);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() + 1);
            System.out.println("Product added to wish list!");
        }
    }
    public void toggleSellerToFollowing(Seller seller) {
        if (!sellersFollowed.contains(seller)) {
            sellersFollowed.add(seller);
            seller.getMetrics().updateLikes(seller.getMetrics().getLikes() + 1);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() + 1);
            seller.addFollower(this);   //seller has a new follower
            addSellersFollowers(seller);        // keep track of buyer's following
            System.out.println("You are now following " + seller.getId() + "!");
        } else {
            sellersFollowed.remove(seller);
            seller.getMetrics().updateLikes(seller.getMetrics().getLikes() - 1);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() - 1);
            seller.removeFollower(this);
            System.out.println("You are no longer following " + seller.getId() + "!");
        }
    }
    public void toggleEvaluationLike(Evaluation evaluation) {
        if (evaluation.getAuthor() == this) {
            System.out.println("You cannot like your own evaluation!");
            return;
        }
        if (!evaluationsLiked.contains(evaluation)) {
            evaluationsLiked.add(evaluation);
            evaluation.setLikes(evaluation.getLikes() + 1);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() + 1);
            System.out.println("You liked " + evaluation.getAuthor().getId() + "'s evaluation!");
        } else {
            evaluationsLiked.remove(evaluation);
            evaluation.setLikes(evaluation.getLikes() - 1);
            this.metrics.setLikesGiven(this.metrics.getLikesGiven() - 1);
            System.out.println("You unliked " + evaluation.getAuthor().getId() + "'s evaluation!");
        }
    }
    public void toggleBuyerToFollowing(Buyer buyer) {
        if (buyer == this) {
            System.out.println("You cannot follow yourself!");
            return;
        }
        if (!buyersFollowed.contains(buyer)) {
            buyersFollowed.add(buyer);
            buyer.getFollowers().add(this);
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
