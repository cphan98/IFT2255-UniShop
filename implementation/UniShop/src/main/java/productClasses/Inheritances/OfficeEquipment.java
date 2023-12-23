package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;
/**
 * The OfficeEquipment class represents an office equipment product in UniShop.
 * It extends the Product class and includes additional attributes specific to office equipment.
 *
 * @author Your Name
 * @version 1.0
 * @since 2023-12-20
 */
public class OfficeEquipment extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private String brand;
    private String model;
    private String subCategory;

    // CONSTRUCTOR

    /**
     * Constructs an OfficeEquipment object with the specified attributes.
     *
     * @param name        The name of the office equipment product.
     * @param description The description of the office equipment product.
     * @param price       The price of the office equipment product.
     * @param basePoints  The base points for the office equipment product.
     * @param seller      The seller of the office equipment product.
     * @param stock       The stock of the office equipment product.
     * @param brand       The brand of the office equipment product.
     * @param model       The model of the office equipment product.
     * @param subCategory The sub-category of the office equipment product.
     * @param sellDate    The sell date of the office equipment product.
     */
    public OfficeEquipment(String name,
                           String description,
                           float price,
                           int basePoints,
                           Seller seller,
                           int stock,
                           String brand,
                           String model,
                           String subCategory,
                           String sellDate) {
        super(name, description, Category.DESKTOP_ACCESSORIES, price, basePoints, seller, stock, sellDate);
        this.brand = brand;
        this.model = model;
        this.subCategory = subCategory;
    }

    // GETTERS
    /**
     * Gets the brand of the office equipment product.
     *
     * @return The brand of the office equipment product.
     */
    public String getBrand() {
        return brand;
    }
    /**
     * Gets the model of the office equipment product.
     *
     * @return The model of the office equipment product.
     */
    public String getModel() {
        return model;
    }
    /**
     * Gets the sub-category of the office equipment product.
     *
     * @return The sub-category of the office equipment product.
     */
    public String getSubCategory() {
        return subCategory;
    }

    // SETTERS
    /**
     * Sets the brand of the office equipment product.
     *
     * @param brand The new brand to set.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }
    /**
     * Sets the model of the office equipment product.
     *
     * @param model The new model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }
    /**
     * Sets the sub-category of the office equipment product.
     *
     * @param subCategory The new sub-category to set.
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
