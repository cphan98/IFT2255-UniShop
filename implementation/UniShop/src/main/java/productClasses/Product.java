package productClasses;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Usages.Evaluation;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Product implements java.io.Serializable {
    // ATTRIBUTES

    private final String id;
    private String title;
    private String description;
    private Category category;
    private float price;
    private int basePoints;
    private Seller seller;
    private int quantity;
    private final String sellDate;
    private final ArrayList<Evaluation> evaluations;
    private float overallRating;
    private int likes;
    private boolean hasPromotion;

    // GETTERS

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }

    public Seller getSeller() {
        return seller;
    }

    public int getBasePoints() {
        return basePoints;
    }

    public float getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Float getOverallRating() {
        return overallRating;
    }

    public ArrayList<Evaluation> getEvaluations() {
        return this.evaluations;
    }

    public int getLikes()
    {
       return this.likes;
    }

    // SETTERS

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setBasePoints(int basePoints) {
        this.basePoints = basePoints;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCategory(String category) {
        this.category = Category.valueOf(category);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPromotion(boolean promotion) {
        hasPromotion = promotion;
    }

    public void setLikes(int likes) {
        this.seller.getMetrics().updateLikes(this.seller.getMetrics().getLikes() - this.likes);
        this.likes = likes;
        this.seller.getMetrics().updateLikes(this.seller.getMetrics().getLikes() + this.likes);
    }

    // CONSTRUCTOR

    public Product(String title, String description, Category category, float price, int basePoints, Seller seller, int quantity, String sellDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.basePoints = basePoints;
        this.seller = seller;
        this.quantity = quantity;
        this.sellDate = sellDate;
        this.evaluations = new ArrayList<>();
        this.overallRating = 0.0F;
        hasPromotion = Math.random() < 0.5;
    }

    // OPERATIONS

    public void updateOverallRating(){
        float total = 0;
        for (Evaluation evaluation : evaluations) {
            total += evaluation.getRating();
        }
        this.overallRating = Math.round((total / evaluations.size()) * 10.0) / 10.0F;
    }

    public String toString() {
        return "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Category: " + category + "\n" +
                "Price: " + price + "$\n" +
                "Base Points: " + basePoints + "\n" +
                "Seller: " + seller.getId() + "\n" +
                "Quantity: " + quantity + "\n" +
                "Sell Date: " + sellDate + "\n" +
                "Likes: " + likes + "\n" + "\n" +
                "Evaluations: " + "\n" +
                evaluationsToString() + "\n" +
                (hasPromotion ? "This product has a promotion" : "This product does not have a promotion") + "\n";
    }

    public String smallToString() {
        return "Title: " + title + "\t\t" + "Price: " + price + "$\t\t" + "Quantity: " + quantity + "\t\t" + "Likes: " + likes + "\t\t" + "Average rating: " + overallRating + "\t\t" + (hasPromotion? "(promoted)" : "") + "\n";
    }

    public String evaluationsToString() {
        if (evaluations.isEmpty()) {
            return "No evaluations yet";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Evaluation evaluation : evaluations) {
            sb.append(i).append(". ").append(evaluation.toString()).append("\n");
            i++;
        }
        return sb.toString();
    }
}
