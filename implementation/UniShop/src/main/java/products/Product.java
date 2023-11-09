package products;
import Users.Seller;
import otherUtility.Category;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Product {
    private final String id;
    private String title;
    private String description;
    private Category category;
    private float price;
    private int basePoints;
    private Seller seller;
    private int quantity;
    private String sellDate;
    private ArrayList<Evaluation> evaluations;
    private float overallRating;
    private int likes;
    //getters and setters
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
    }


    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }
    public Seller getSeller() {
        return seller;
    }
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    public int getBasePoints() {
        return basePoints;
    }
    public void setBasePoints(int basePoints) {
        this.basePoints = basePoints;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setCategory(String category) {
        this.category = Category.valueOf(category);
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Float getOverallRating() {
        return overallRating;
    }
    public void updateOverallRating(){
        float total = 0;
        for (Evaluation evaluation : evaluations) {
            total += evaluation.getRating();
        }
        this.overallRating = total / evaluations.size();
    }

    public ArrayList<Evaluation> getEvaluations() {
        return this.evaluations;
    }
    public void addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
        updateOverallRating();
    }
    public int getLikes()
    {
       return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
                evaluationsToString() + "\n";
    }

    public String smallToString() {
        return "Title: " + title + "\t\t" + "Price: " + price + "$\t\t" + "Quantity: " + quantity + "\t\t" + "Likes: " + likes + "\n";
    }

    public String evaluationsToString() {
        if (evaluations.isEmpty()) {
            return "No evaluations yet";
        }
        StringBuilder sb = new StringBuilder();
        for (Evaluation evaluation : evaluations) {
            sb.append(evaluation.toString()).append("\n");
        }
        return sb.toString();
    }
}
