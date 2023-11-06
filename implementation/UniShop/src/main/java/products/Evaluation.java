package products;

import Users.Buyer;

public class Evaluation {
    private String comment;
    private Float rating;
    private Buyer author;

    public Evaluation(String comment, Float rating, Buyer author)
    {
        this.comment = comment;
        this.rating = rating;
        this.author = author;
    }
    //getters (no setters because we don't want to change the comment, rating or author)
    public String getComment() {
        return comment;
    }
    public float getRating() {
        return rating;
    }
    public Buyer getAuthor() {
        return author;
    }


}
