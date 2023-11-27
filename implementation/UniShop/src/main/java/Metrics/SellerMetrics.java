package Metrics;

import java.util.ArrayList;

public class SellerMetrics implements Metrics, java.io.Serializable {
    private float revenue;
    private int productsSold;
    private float averageNoteReceived;
    private int likes;
    protected ArrayList<Float> notesReceived;
    public SellerMetrics() {
        revenue = 0;
        productsSold = 0;
        averageNoteReceived = 0;
        notesReceived = new ArrayList<>();
    }
    public void updateRevenue(float revenue) {
        this.revenue = revenue;
    }
    public void updateProductsSold(int productsSold) {
        this.productsSold = productsSold;
    }
    public void updateAverageNoteReceived(float note) {
        notesReceived.add(note);
        float sum = 0;
        for (float n : notesReceived) {
            sum += n;
        }
        averageNoteReceived = Math.round((sum / notesReceived.size()) * 10) / 10f;
    }
    public void updateLikes(int likes) {
        this.likes = likes;
    }
    public float getRevenue() {
        return revenue;
    }
    public int getProductsSold() {
        return productsSold;
    }
    public float getAverageNoteReceived() {
        return averageNoteReceived;
    }
    public int getLikes() {
        return likes;
    }

    public String toString() {
        return "Since inception," + "\n" +
            "Income: " + revenue + "$\n" +
            "Products sold: " + productsSold + "\n" +
            "Average note received: " + averageNoteReceived + "\n" +
            "Total of likes received on products: " + likes + "\n";
    }
}
