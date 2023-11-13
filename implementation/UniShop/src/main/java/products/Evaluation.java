package products;

import Users.Buyer;

public class Evaluation {
    private String comment;
    private Float rating;
    private Buyer author;
    private int likes;

    public Evaluation(String comment, Float rating, Buyer author)
    {
        this.comment = comment;
        this.rating = rating;
        this.author = author;
        this.likes = 0;
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
    public int getLikes() {
        return likes;
    }
    //methods
    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String toString() {
        return "Evaluation: " + comment + "\t\tRating: " + rating + "\t\tAuthor: " + author.getFirstName() + " " + author.getLastName() + "\t\t Likes: " + likes + "\n";
    }


}
