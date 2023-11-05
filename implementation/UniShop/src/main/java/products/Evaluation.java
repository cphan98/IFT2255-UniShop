package products;

public class Evaluation {

    public void addComment(Product product, String comment)
    {
        product.getComment().add(comment);
    }

    public void addRating(Product product, Float rating)
    {
        product.getRating().add(rating);
    }

    public void addLikes(Product product)
    {
        int likes = product.getLikes() + 1;
        product.setLikes(likes);
    }

    public void removeLikes(Product product)
    {
        int likes = product.getLikes() - 1;
        product.setLikes(likes);
    }
}
