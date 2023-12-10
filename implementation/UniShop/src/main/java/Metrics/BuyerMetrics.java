package Metrics;

import BackEndUtility.InputManager;

import java.util.ArrayList;

public class BuyerMetrics implements Metrics, java.io.Serializable {
    private int ordersMade;
    private int productsBought;
    private int likesReceived;
    private int likesGiven;
    private int evaluationsMade;
    private final ArrayList<Float> notesGiven;
    private float averageNoteGiven;

    private int buyPoints;
    private int expPoints;

    public ArrayList<String> selectedMetrics; //to be shown on the users profile

    public BuyerMetrics() {
        ordersMade = 0;
        productsBought = 0;
        likesReceived = 0;
        likesGiven = 0;
        evaluationsMade = 0;
        averageNoteGiven = 0;
        expPoints = 0;
        buyPoints = 0;
        notesGiven = new ArrayList<>();
        selectedMetrics = new ArrayList<>();
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
    public void setSelectedMetrics(ArrayList<String> selectedMetrics) {this.selectedMetrics = selectedMetrics; };


    public void addExpPoints(int points) {
        this.expPoints += points;
    }
    public void removeExpPoints(int points) {
        this.expPoints -= points;
    }
    public int getExpPoints(){ return expPoints;}
    public void addBuyPoints(int points) {
        this.buyPoints += points;
    }
    public void removeBuyPoints(int points) {
        this.buyPoints -= points;
    }
    public int getBuyPoints() {
        return buyPoints;
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
    public ArrayList<String> getSelectedMetrics(){
        return selectedMetrics;
    }
    protected int getUserInputAsInteger() {
        while (true) {
            try {
                int returned = Integer.parseInt(InputManager.getInstance().nextLine());
                if (returned < 0) {
                    System.out.println("Invalid input. Please enter a positive number.");
                } else {
                    return returned;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

        }
    }
    public void updateAverageNoteGiven(float note) {
        notesGiven.add(note);
        float sum = 0;
        for (float n : notesGiven) {
            sum += n;
        }
        averageNoteGiven = sum / notesGiven.size();
    }

    public void removeAverageNoteGiven(float note) {
        notesGiven.remove(note);
        float sum = 0;
        for (float n : notesGiven) {
            sum += n;
        }
        averageNoteGiven = sum / notesGiven.size();
    }

    public String AlltoString() {
        return
                "Since inception," + "\n" +
            "Orders made: " + ordersMade + "\n" +
            "Products bought: " + productsBought + "\n" +
            "Likes received on their evaluations: " + likesReceived + "\n" +
            "Likes given: " + likesGiven + "\n" +
            "Evaluations made: " + evaluationsMade + "\n" +

            "Average note given: " + Math.round(averageNoteGiven*10)/10 + "\n" +
            "Experience: " + expPoints + " points\n";

    }
    public void configureMetrics(){
        ArrayList<String> allMetrics = new ArrayList<>();
        ArrayList<Number> allMetricsValues =new ArrayList<>();
        allMetrics.add("Orders made : ");
        allMetricsValues.add(ordersMade);
        allMetrics.add("Products bought : " );
        allMetricsValues.add(productsBought);
        allMetrics.add("Likes received on evaluation : " );
        allMetricsValues.add(likesReceived);
        allMetrics.add("Likes given : " );
        allMetricsValues.add(likesGiven);
        allMetrics.add("Average note given : " );
        allMetricsValues.add(averageNoteGiven);
        allMetrics.add("Experience points : " );
        allMetricsValues.add(expPoints);
        selectMetrics(allMetrics, allMetricsValues);
    }

    private void selectMetrics(ArrayList<String> allMetrics, ArrayList<Number> allMetricsValues){
        System.out.println("Select metrics to be displayed in your profile (3 max.)");
        System.out.println("Enter the number corresponding to the metric to select or '0' to finish:");
        int count = 1;
        for (String metric : allMetrics){
            System.out.println(count + ". " + metric);
            count++;
        }
        while (true) {
            int choice = getUserInputAsInteger();
            if (choice == 0){
                return;
            }

            if (choice > 0 && choice <= allMetrics.size()){
                String metricSelected = allMetrics.get(choice - 1)  ;
                if(!selectedMetrics.contains(metricSelected)){
                    selectedMetrics.add(metricSelected + " " + allMetricsValues.get(choice-1).toString());
                    System.out.println(metricSelected + " was added to your profile");
                    if (selectedMetrics.size()==3){
                        System.out.println("--------------------------------------------------------------");
                        System.out.println("Your selected metrics have been successfully added to your profile!");
                        System.out.println("--------------------------------------------------------------");
                        System.out.println();
                        return;
                    }
                } else {
                    System.out.println("Metric already displayed in profile");
                }
            } else {
                System.out.println("invalid selection");
            }
        }
    }

    public String toString() {
        return
                "Orders made: " + ordersMade + "\n" +
            "Products bought: " + productsBought + "\n" +
            "Likes received on their evaluations: " + likesReceived + "\n" +
            "Likes given: " + likesGiven + "\n" +
            "Evaluations made: " + evaluationsMade + "\n" +
            "Average note given: " + Math.round(averageNoteGiven*10)/10 + "\n" +
            "Experience: " + expPoints + " points\n";
    }


}
