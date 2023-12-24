package productClasses;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Usages.Evaluation;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents the blueprint of any products sold on UniShop.
 *
 * A product can be specialized into a book, hardware, learning resource, office equipment or stationary product.
 *
 * This class contains basic methods, such as updating the overall notes for this product and converting data to string
 * in order to display them properly.
 */
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
    private boolean hasPromotion = false;

    // CONSTRUCTOR

    /**
     * Constructs an instance of Product with given title, description, category, price, base points, seller, available
     * quantity and sell date.
     *
     * @param title         String, product's title
     * @param description   String, product's description
     * @param category      Category, product's category
     * @param price         float, product's price
     * @param basePoints    int, product's base points
     * @param seller        Seller, product's seller
     * @param quantity      int, available number of this product
     * @param sellDate      String, product's sell date
     */
    public Product(String title,
                   String description,
                   Category category,
                   float price,
                   int basePoints,
                   Seller seller,
                   int quantity,
                   String sellDate) {
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
    }

    // GETTERS

    /**
     * Returns the product's ID. An ID is unique.
     *
     * @return  String, unique ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the product's title.
     *
     * @return  String, title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the product's available quantity.
     *
     * @return  int, available product quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the product's seller.
     *
     * @return  Seller, seller
     */
    public Seller getSeller() {
        return seller;
    }

    /**
     * Returns the product's base points.
     *
     * @return  int, base points
     */
    public int getBasePoints() {
        return basePoints;
    }

    /**
     * Returns the product's price.
     *
     * @return  float, price
     */
    public float getPrice() {
        return price;
    }

    /**
     * Returns the product's category.
     *
     * @return Category, category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the product's description.
     *
     * @return String, description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the product's overall rating.
     *
     * @return  float, overall rating
     */
    public float getOverallRating() {
        return overallRating;
    }

    /**
     * Returns the product's list of evaluations.
     *
     * @return  ArrayList of Evaluation, evaluations
     */
    public ArrayList<Evaluation> getEvaluations() {
        return this.evaluations;
    }

    /**
     * Returns the product's total number of likes received.
     *
     * @return  int, total number of likes received
     */
    public int getLikes()
    {
       return this.likes;
    }

    // SETTERS

    /**
     * Sets the product's title.
     *
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the product's available quantity.
     *
     * @param quantity  int
     */
    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    /**
     * Sets the product's seller.
     * @param seller    Seller
     */
    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    /**
     * Sets the product's base points.
     *
     * @param basePoints    int
     */
    public void setBasePoints(int basePoints) {
        this.basePoints = basePoints;
    }

    /**
     * Sets the product's price.
     *
     * @param price float
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Sets the product's category.
     *
     * @param category  Category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Sets the product's category. The given string will be matched with an enum constant in Category.
     *
     * @param category String
     */
    public void setCategory(String category) {
        this.category = Category.valueOf(category);
    }

    /**
     * Sets the product's description.
     *
     * @param description   String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets hasPromotion to true when the product is promoted.
     *
     * @param promotion Boolean
     */
    public void setPromotion(boolean promotion) {
        hasPromotion = promotion;
    }

    /**
     * Sets the product's total number of likes received.
     *
     * @param likes int
     */
    public void setLikes(int likes) {
        this.seller.getMetrics().updateLikes(this.seller.getMetrics().getLikes() - this.likes);
        this.likes = likes;
        this.seller.getMetrics().updateLikes(this.seller.getMetrics().getLikes() + this.likes);
    }

    // UTILITIES

    // rating ---------------------------------------------------------------------------------------------------------

    /**
     * Updates the product's overall rating.
     */
    public void updateOverallRating(){
        float total = 0;
        for (Evaluation evaluation : evaluations) {
            total += evaluation.getRating();
        }
        this.overallRating = Math.round((total / evaluations.size()) * 10.0) / 10.0F;
    }

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Returns a summary of the product.
     *
     * @return String
     */
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

    /**
     * Returns the product's title, price, available quantities, likes and average rating. If the product is promoted,
     * the product will be marked as "(promoted)".
     *
     * @return String
     */
    public String smallToString() {
        return "Title: " + title + "\t\t" + "Price: " + price + "$\t\t" + "Quantity: " + quantity + "\t\t"
                + "Likes: " + likes + "\t\t" + "Average rating: " + overallRating + "\t\t"
                + (hasPromotion? "(promoted)" : "") + "\n";
    }

    /**
     * Returns the product's evaluations.
     *
     * @return  String
     */
    private String evaluationsToString() {
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
