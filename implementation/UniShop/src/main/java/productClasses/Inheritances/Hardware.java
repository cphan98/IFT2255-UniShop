package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;
/**
 * The Hardware class represents a hardware product in UniShop.
 * It extends the Product class and includes additional attributes specific to hardware.
 *
 * @author Your Name
 * @version 1.0
 * @since 2023-12-20
 */
public class Hardware extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private String brand;
    private String model;
    private String releaseDate;
    private String subCategory;

    // CONSTRUCTOR
    /**
     * Constructs a Hardware object with the specified attributes.
     *
     * @param name        The name of the hardware product.
     * @param description The description of the hardware product.
     * @param price       The price of the hardware product.
     * @param basePoints  The base points for the hardware product.
     * @param seller      The seller of the hardware product.
     * @param stock       The stock of the hardware product.
     * @param brand       The brand of the hardware product.
     * @param model       The model of the hardware product.
     * @param releaseDate The release date of the hardware product.
     * @param subCategory The sub-category of the hardware product.
     * @param sellDate    The sell date of the hardware product.
     */
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
    /**
     * Gets the brand of the hardware product.
     *
     * @return The brand of the hardware product.
     */
    public String getBrand() {
        return brand;
    }
    /**
     * Gets the model of the hardware product.
     *
     * @return The model of the hardware product.
     */
    public String getModel() {
        return model;
    }
    /**
     * Gets the release date of the hardware product.
     *
     * @return The release date of the hardware product.
     */
    public String getReleaseDate() {
        return releaseDate;
    }
    /**
     * Gets the sub-category of the hardware product.
     *
     * @return The sub-category of the hardware product.
     */
    public String getSubCategory() {
        return subCategory;
    }

    // SETTERS
    /**
     * Sets the brand of the hardware product.
     *
     * @param brand The new brand to set.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }
    /**
     * Sets the model of the hardware product.
     *
     * @param model The new model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }
    /**
     * Sets the release date of the hardware product.
     *
     * @param releaseDate The new release date to set.
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    /**
     * Sets the sub-category of the hardware product.
     *
     * @param subCategory The new sub-category to set.
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
