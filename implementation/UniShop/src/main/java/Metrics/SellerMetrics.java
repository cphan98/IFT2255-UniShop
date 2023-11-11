package Metrics;

import java.util.ArrayList;

public class SellerMetrics implements Metrics {
    private float revenue;
    private int productsSold;
    private float averageNoteReceived;
    protected ArrayList<Float> notesReceived;
    public SellerMetrics() {
        revenue = 0;
        productsSold = 0;
        averageNoteReceived = 0;
    }
    public SellerMetrics(float revenue, int productsSold, float averageNoteReceived) {
        this.revenue = revenue;
        this.productsSold = productsSold;
        this.averageNoteReceived = averageNoteReceived;
    }

    public void updateRevenue(float revenue) {
        this.revenue = revenue;
    }
    public void updateProductsSold(int productsSold) {
        this.productsSold = productsSold;
    }
    public void updateAverageNoteReceived(float note) {
        notesReceived = new ArrayList<>();
        notesReceived.add(note);
        float sum = 0;
        for (float n : notesReceived) {
            sum += n;
        }
        averageNoteReceived = sum / notesReceived.size();

    }

    public String toString() {
        return "Since inception," + "\n" +
            "Income: " + revenue + "\n" +
            "Products sold: " + productsSold + "\n" +
            "Average note received: " + averageNoteReceived + "\n";
    }
}
