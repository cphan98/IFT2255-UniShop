package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;

public class Stationery extends Product {
    private String brand;
    private String model;
    private String subCategory;

    public Stationery(String name, String description, float price, int basePoints, Seller seller, int stock, String brand, String model, String subCategory, String releaseDate, String sellDate) {
        super(name, description, Category.STATIONERY, price, basePoints, seller, stock, sellDate);
        this.brand = brand;
        this.model = model;
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

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

}
