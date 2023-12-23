package productClasses.Inheritances;

import Users.Seller;
import BackEndUtility.Category;
import productClasses.Product;
/**
 * The Book class represents a book product in UniShop.
 * It extends the Product class and includes additional attributes specific to books.
 *
 */
public class Book extends Product implements java.io.Serializable {

    // ATTRIBUTES

    private int ISBN;
    private String author;
    private String publishingHouse;
    private String genre;
    private String releaseDate;
    private int edition;
    private int volume;

    // CONSTRUCTOR
    /**
     * Constructs a Book object with the specified attributes.
     *
     * @param name            The name of the book.
     * @param description     The description of the book.
     * @param price           The price of the book.
     * @param basePoints      The base points for the book.
     * @param seller          The seller of the book.
     * @param stock           The stock of the book.
     * @param ISBN            The ISBN (International Standard Book Number) of the book.
     * @param author          The author of the book.
     * @param publishingHouse The publishing house of the book.
     * @param genre           The genre of the book.
     * @param sellDate        The sell date of the book.
     * @param releaseDate     The release date of the book.
     * @param edition         The edition of the book.
     * @param volume          The volume of the book.
     */
    public Book(String name,
                String description,
                float price,
                int basePoints,
                Seller seller,
                int stock,
                int ISBN,
                String author,
                String publishingHouse,
                String genre,
                String sellDate,
                String releaseDate,
                int edition,
                int volume) {
        super(name, description, Category.BOOKS, price, basePoints, seller, stock, sellDate);
        this.ISBN = ISBN;
        this.author = author;
        this.publishingHouse = publishingHouse;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.edition = edition;
        this.volume = volume;
    }

    // GETTERS
    /**
     * Gets the ISBN (International Standard Book Number) of the book.
     *
     * @return The ISBN of the book.
     */
    public int getISBN() {
        return ISBN;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book.
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Gets the publishing house of the book.
     *
     * @return The publishing house of the book.
     */
    public String getPublishingHouse() {
        return publishingHouse;
    }
    /**
     * Gets the genre of the book.
     *
     * @return The genre of the book.
     */
    public String getGenre(){
        return genre;
    }
    /**
     * Gets the release date of the book.
     *
     * @return The release date of the book.
     */
    public String getReleaseDate() {
        return releaseDate;
    }
    /**
     * Gets the edition of the book.
     *
     * @return The edition of the book.
     */
    public int getEdition() {
        return edition;
    }
    /**
     * Gets the volume of the book.
     *
     * @return The volume of the book.
     */
    public int getVolume() {
        return volume;
    }

    // SETTERS
    /**
     * Sets the author of the book.
     *
     * @param author The new author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * Sets the publishing house of the book.
     *
     * @param publishingHouse The new publishing house to set.
     */
    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }
    /**
     * Sets the genre of the book.
     *
     * @param genre The new genre to set.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
    /**
     * Sets the release date of the book.
     *
     * @param releaseDate The new release date to set.
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    /**
     * Sets the edition of the book.
     *
     * @param edition The new edition to set.
     */
    public void setEdition(int edition) {
        this.edition = edition;
    }
    /**
     * Sets the volume of the book.
     *
     * @param volume The new volume to set.
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }
}
