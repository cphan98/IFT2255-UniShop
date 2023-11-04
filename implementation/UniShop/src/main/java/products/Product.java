package products;
import Users.Seller;
import otherUtility.Category;

import java.util.UUID;

public abstract class Product {
    private String id;
    private String title;
    private String description;
    private Category category;
    private float price;
    private int basePoints;
    private Seller seller;
    private int quantity;
    private String sellDate;

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
    public String toString() {
        return "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Category: " + category + "\n" +
                "Price: " + price + "\n" +
                "Base Points: " + basePoints + "\n" +
                "Seller: " + seller.getId() + "\n" +
                "Quantity: " + quantity + "\n" +
                "Sell Date: " + sellDate + "\n";
    }
}
