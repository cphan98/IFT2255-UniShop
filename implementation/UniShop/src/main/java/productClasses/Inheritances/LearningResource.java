package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;
/**
 * The LearningResource class represents a learning resource product in UniShop.
 * It extends the {@link Product} class and includes additional attributes specific to learning resources.
 *
 */
public class LearningResource extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private int ISBN;
    private String author;
    private String organization;
    private String releaseDate;
    private String type;
    private int edition;

    // CONSTRUCTOR
    /**
     * Constructs a LearningResource object with the specified attributes.
     *
     * @param name        The name of the learning resource product.
     * @param description The description of the learning resource product.
     * @param price       The price of the learning resource product.
     * @param basePoints  The base points for the learning resource product.
     * @param seller      The seller of the learning resource product.
     * @param stock       The stock of the learning resource product.
     * @param ISBN        The ISBN of the learning resource product.
     * @param author      The author of the learning resource product.
     * @param organization The organization of the learning resource product.
     * @param sellDate    The sell date of the learning resource product.
     * @param releaseDate The release date of the learning resource product.
     * @param type        The type of the learning resource product.
     * @param edition     The edition of the learning resource product.
     */
    public LearningResource(String name,
                            String description,
                            float price,
                            int basePoints,
                            Seller seller,
                            int stock, int ISBN,
                            String author,
                            String organization,
                            String sellDate,
                            String releaseDate,
                            String type,
                            int edition) {
        super(name, description, Category.LEARNING_RESOURCES, price, basePoints, seller, stock, sellDate);
        this.ISBN = ISBN;
        this.author = author;
        this.organization = organization;
        this.releaseDate = releaseDate;
        this.type = type;
        this.edition = edition;
    }


    // GETTERS
    /**
     * Gets the ISBN of the learning resource product.
     *
     * @return The ISBN of the learning resource product.
     */
    public int getISBN() {
        return ISBN;
    }
    /**
     * Gets the author of the learning resource product.
     *
     * @return The author of the learning resource product.
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Gets the organization of the learning resource product.
     *
     * @return The organization of the learning resource product.
     */
    public String getOrganization() {
        return organization;
    }
    /**
     * Gets the release date of the learning resource product.
     *
     * @return The release date of the learning resource product.
     */
    public String getReleaseDate() {
        return releaseDate;
    }
    /**
     * Gets the type of the learning resource product.
     *
     * @return The type of the learning resource product.
     */
    public String getType() {
        return type;
    }
    /**
     * Gets the edition of the learning resource product.
     *
     * @return The edition of the learning resource product.
     */
    public int getEdition() {
        return edition;
    }

    // SETTERS
    /**
     * Sets the ISBN of the learning resource product.
     *
     * @param ISBN The new ISBN to set.
     */
    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }
    /**
     * Sets the author of the learning resource product.
     *
     * @param author The new author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * Sets the organization of the learning resource product.
     *
     * @param organization The new organization to set.
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    /**
     * Sets the release date of the learning resource product.
     *
     * @param releaseDate The new release date to set.
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    /**
     * Sets the type of the learning resource product.
     *
     * @param type The new type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Sets the edition of the learning resource product.
     *
     * @param edition The new edition to set.
     */
    public void setEdition(int edition) {
        this.edition = edition;
    }
}
