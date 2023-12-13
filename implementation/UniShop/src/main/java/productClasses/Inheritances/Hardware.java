package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;

public class Hardware extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private String brand;
    private String model;
    private String releaseDate;
    private String subCategory;

    // CONSTRUCTOR

    public Hardware(String name,
                    String description,
                    float price,
                    int basePoints,
                    Seller seller,
                    int stock,
                    String brand,
                    String model,
                    String releaseDate,
                    String subCategory,
                    String sellDate) {
        super(name, description, Category.ELECTRONICS, price, basePoints, seller, stock, sellDate);
        this.brand = brand;
        this.model = model;
        this.releaseDate = releaseDate;
        this.subCategory = subCategory;
    }

    // GETTERS

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSubCategory() {
        return subCategory;
    }

    // SETTERS

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
