package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;
/**
 * The Stationery class represents a stationery product in an online marketplace.
 * It extends the Product class and includes additional attributes specific to stationery.
 *
 */
public class Stationery extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private String brand;
    private String model;
    private String subCategory;

    // CONSTRUCTOR
    /**
     * Constructs a Stationery object with the specified attributes.
     *
     * @param name        The name of the stationery product.
     * @param description The description of the stationery product.
     * @param price       The price of the stationery product.
     * @param basePoints  The base points for the stationery product.
     * @param seller      The seller of the stationery product.
     * @param stock       The stock of the stationery product.
     * @param brand       The brand of the stationery product.
     * @param model       The model of the stationery product.
     * @param subCategory The sub-category of the stationery product.
     * @param releaseDate The release date of the stationery product.
     * @param sellDate    The sell date of the stationery product.
     */
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
    /**
     * Gets the brand of the stationery product.
     *
     * @return The brand of the stationery product.
     */
    public String getBrand() {
        return brand;
    }
    /**
     * Gets the model of the stationery product.
     *
     * @return The model of the stationery product.
     */
    public String getModel() {
        return model;
    }
    /**
     * Gets the sub-category of the stationery product.
     *
     * @return The sub-category of the stationery product.
     */
    public String getSubCategory() {
        return subCategory;
    }

    // SETTERS
    /**
     * Sets the brand of the stationery product.
     *
     * @param brand The new brand to set.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }
    /**
     * Sets the model of the stationery product.
     *
     * @param model The new model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }
    /**
     * Sets the sub-category of the stationery product.
     *
     * @param subCategory The new sub-category to set.
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

}
