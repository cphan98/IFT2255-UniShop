package Metrics;

public class SellerMetrics implements Metrics {
    private float revenue;
    private int productsSold;
    private float averageNoteReceived;
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
    public void updateAverageNoteReceived(float averageNoteReceived) {
        this.averageNoteReceived = averageNoteReceived;
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

    public String toString() {
        return "Since inception," + "\n" +
            "Income: " + revenue + "$\n" +
            "Products sold: " + productsSold + "\n" +
            "Average note received: " + averageNoteReceived + "\n";
    }
}
