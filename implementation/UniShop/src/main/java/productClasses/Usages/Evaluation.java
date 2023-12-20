package productClasses.Usages;

import Users.Buyer;

/**
 * Class that represents an evaluation of a product. It an author, a comment made by the author and a rating given by him too.
 */
public class Evaluation implements java.io.Serializable {

    // ATTRIBUTES

    private final String comment;
    private final Float rating;
    private final Buyer author;
    private int likes;

    // CONSTRUCTOR

    /**
     * Constructor of the class Evaluation
     *
     * @param comment the comment of the evaluation
     * @param rating the rating given by the author
     * @param author the author of the evaluation
     */
    public Evaluation(String comment, Float rating, Buyer author) {
        this.comment = comment;
        this.rating = rating;
        this.author = author;
        this.likes = 0;
    }

    // GETTERS (no setters because we don't want to change the comment, rating or author)

    /**
     * Method that returns the rating given by the author
     *
     * @return the rating of the evaluation
     */
    public float getRating() {
        return rating;
    }

    /**
     * Method that returns the comment made by the author
     *
     * @return the comment of the evaluation
     */
    public Buyer getAuthor() {
        return author;
    }

    /**
     * Method that returns the number of likes received by the evaluation
     *
     * @return the likes received
     */
    public int getLikes() {
        return likes;
    }

    // SETTER

    /**
     * Method that sets the number of likes received by the evaluation
     *
     * @param likes the number of likes received
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    // UTILITIES

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Method that returns a string representation of the evaluation
     *
     * @return the string representation of the evaluation
     */
    public String toString() {
        return "Evaluation: " + comment + "\t\tRating: " + rating + "\t\tAuthor: " + author.getFirstName() + " "
                + author.getLastName() + "\t\t Likes: " + likes + "\n";
    }
}
