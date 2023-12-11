package productClasses.Usages;

import Users.Buyer;

public class Evaluation implements java.io.Serializable {

    // ATTRIBUTES

    private final String comment;
    private final Float rating;
    private final Buyer author;
    private int likes;

    // CONSTRUCTOR

    public Evaluation(String comment, Float rating, Buyer author)
    {
        this.comment = comment;
        this.rating = rating;
        this.author = author;
        this.likes = 0;
    }

    // GETTERS (no setters because we don't want to change the comment, rating or author)

    public float getRating() {
        return rating;
    }

    public Buyer getAuthor() {
        return author;
    }

    public int getLikes() {
        return likes;
    }

    // SETTER

    public void setLikes(int likes) {
        this.likes = likes;
    }

    // UTILITIES

    // to string ------------------------------------------------------------------------------------------------------

    public String toString() {
        return "Evaluation: " + comment + "\t\tRating: " + rating + "\t\tAuthor: " + author.getFirstName() + " "
                + author.getLastName() + "\t\t Likes: " + likes + "\n";
    }
}
