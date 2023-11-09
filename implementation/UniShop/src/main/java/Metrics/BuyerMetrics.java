package Metrics;

import java.util.ArrayList;

public class BuyerMetrics implements Metrics {
    private int ordersMade;
    private int productsBought;
    private int likesReceived;
    private int likesGiven;
    private int evaluationsMade;
    private ArrayList<Float> notesGiven;
    private float averageNoteGiven;
    private int points;

    public BuyerMetrics() {
        ordersMade = 0;
        productsBought = 0;
        likesReceived = 0;
        likesGiven = 0;
        evaluationsMade = 0;
        averageNoteGiven = 0;
        points = 0;
        notesGiven = new ArrayList<>();
    }

    public BuyerMetrics(int ordersMade, int productsBought, int likesReceived, int likesGiven, int evaluationsMade, float averageNoteGiven, ArrayList<Float> notesGiven) {
        this.ordersMade = ordersMade;
        this.productsBought = productsBought;
        this.likesReceived = likesReceived;
        this.likesGiven = likesGiven;
        this.evaluationsMade = evaluationsMade;
        this.averageNoteGiven = averageNoteGiven;
        this.notesGiven = notesGiven;
        this.points = 0;
    }

    public void setOrdersMade(int ordersMade) {
        this.ordersMade = ordersMade;
    }
    public void setProductsBought(int productsBought) {
        this.productsBought = productsBought;
    }
    public void setLikesReceived(int likesReceived) {
        this.likesReceived = likesReceived;
    }
    public void setLikesGiven(int likesGiven) {
        this.likesGiven = likesGiven;
    }
    public void setEvaluationsMade(int evaluationsMade) {
        this.evaluationsMade = evaluationsMade;
    }
    public void setAverageNoteGiven(float averageNoteGiven) {
        this.averageNoteGiven = averageNoteGiven;
    }
    public int getOrdersMade() {
        return ordersMade;
    }
    public int getProductsBought() {
        return productsBought;
    }
    public int getLikesReceived() {
        return likesReceived;
    }
    public int getLikesGiven() {
        return likesGiven;
    }
    public int getEvaluationsMade() {
        return evaluationsMade;
    }
    public float getAverageNoteGiven() {
        return averageNoteGiven;
    }
    public int getPoints() {
        return points;
    }
    public void addPoints(int points) {
        this.points += points;
    }

    public void updateAverageNoteGiven(float note) {
        notesGiven.add(note);
        float sum = 0;
        for (float n : notesGiven) {
            sum += n;
        }
        averageNoteGiven = sum / notesGiven.size();
    }

    public String toString() {
        return
                "Since inception," + "\n" +
            "Orders made: " + ordersMade + "\n" +
            "Products bought: " + productsBought + "\n" +
            "Likes received on their evaluations: " + likesReceived + "\n" +
            "Likes given: " + likesGiven + "\n" +
            "Evaluations made: " + evaluationsMade + "\n" +
            "Average note given: " + averageNoteGiven + "\n" + "\n" +
            "Points: " + points + "\n";
    }
}
