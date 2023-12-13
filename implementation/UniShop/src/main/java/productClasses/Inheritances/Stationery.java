package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;

public class Stationery extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private String brand;
    private String model;
    private String subCategory;

    // CONSTRUCTOR

    public Stationery(String name,
                      String description,
                      float price,
                      int basePoints,
                      Seller seller,
                      int stock,
                      String brand,
                      String model,
                      String subCategory,
                      String releaseDate,
                      String sellDate) {
        super(name, description, Category.STATIONERY, price, basePoints, seller, stock, sellDate);
        this.brand = brand;
        this.model = model;
        this.subCategory = subCategory;
    }

    // GETTERS

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
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

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

}
