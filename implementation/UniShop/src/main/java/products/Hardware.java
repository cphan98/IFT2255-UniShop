package products;

import Users.Seller;
import otherUtility.Category;

public class Hardware extends Product {
    private String brand;
    private String model;
    private String releaseDate;
    private String subCategory;

    public Hardware(String name, String description, float price, int basePoints, Seller seller, int stock, String brand, String model, String releaseDate, String subCategory, String sellDate) {
        super(name, description, Category.ELECTRONICS, price, basePoints, seller, stock, sellDate);
        this.brand = brand;
        this.model = model;
        this.releaseDate = releaseDate;
        this.subCategory = subCategory;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
