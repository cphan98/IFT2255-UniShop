package Metrics;

public class BuyerMetrics implements Metrics {
    private int ordersMade;
    private int productsBought;
    private int likesReceived;
    private int likesGiven;
    private int evaluationsMade;
    private float averageNoteGiven;

    public BuyerMetrics() {
        ordersMade = 0;
        productsBought = 0;
        likesReceived = 0;
        likesGiven = 0;
        evaluationsMade = 0;
        averageNoteGiven = 0;
    }

    public BuyerMetrics(int ordersMade, int productsBought, int likesReceived, int likesGiven, int evaluationsMade, float averageNoteGiven) {
        this.ordersMade = ordersMade;
        this.productsBought = productsBought;
        this.likesReceived = likesReceived;
        this.likesGiven = likesGiven;
        this.evaluationsMade = evaluationsMade;
        this.averageNoteGiven = averageNoteGiven;
    }

    public void updateOrdersMade(int ordersMade) {
        this.ordersMade = ordersMade;
    }
    public void updateProductsBought(int productsBought) {
        this.productsBought = productsBought;
    }
    public void updateLikesReceived(int likesReceived) {
        this.likesReceived = likesReceived;
    }
    public void updateLikesGiven(int likesGiven) {
        this.likesGiven = likesGiven;
    }
    public void updateEvaluationsMade(int evaluationsMade) {
        this.evaluationsMade = evaluationsMade;
    }
    public void updateAverageNoteGiven(float averageNoteGiven) {
        this.averageNoteGiven = averageNoteGiven;
    }

    public String toString() {
        return
                "Since inception," + "\n" +
            "Orders made: " + ordersMade + "\n" +
            "Products bought: " + productsBought + "\n" +
            "Likes received on their evaluations: " + likesReceived + "\n" +
            "Likes given: " + likesGiven + "\n" +
            "Evaluations made: " + evaluationsMade + "\n" +
            "Average note given: " + averageNoteGiven + "\n";
    }
}
