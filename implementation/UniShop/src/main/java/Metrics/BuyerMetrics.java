package Metrics;

import BackEndUtility.InputManager;

import java.util.ArrayList;

/**
 * Represents the buyer's metrics. Metrics include: number of orders made, number of products bought, number of likes received,
 * number of likes given, number of evaluations made, list of all the notes gives, the average not given and number of buy points,
 * number of experience points.
 *
 * This class also includes a list of selected metrics to display in the buyer's profile.
 *
 * BuyerMetrics updates the buyer's metrics, and contains methods converting data to strings to properly display them.
 *
 * This class implements Metrics and java.io.Serializable.
 */
public class BuyerMetrics implements Metrics, java.io.Serializable {

    // ATTRIBUTES

    private int ordersMade;
    private int productsBought;
    private int likesReceived;
    private int likesGiven;
    private int evaluationsMade;
    private final ArrayList<Float> notesGiven;
    private float averageNoteGiven;
    private int buyPoints;
    private int expPoints;
    private ArrayList<String> selectedMetrics; //to be shown on the users profile

    // CONSTRUCTOR

    /**
     * Constructs an instance of BuyerMetrics.
     */
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

    // GETTERS

    /**
     * Returns the buyer's experience points.
     *
     * @return  int, experience points
     */
    public int getExpPoints(){ return expPoints;}

    /**
     * Returns the buyer's buy points.
     *
     * @return  int, buy points
     */
    public int getBuyPoints() {
        return buyPoints;
    }

    /**
     * Returns the number of orders the buyer made.
     *
     * @return  int, number of order's made
     */
    public int getOrdersMade() {
        return ordersMade;
    }

    /**
     * Returns the number of products the buyer bought.
     *
     * @return  int, number of products bought
     */
    public int getProductsBought() {
        return productsBought;
    }

    /**
     * Returns the total number of likes the buyer received. The buyer receives likes when another buyer follows them,
     * and/or when another buyer likes an evaluation this buyer made.
     *
     * @return int, number of likes received
     */
    public int getLikesReceived() {
        return likesReceived;
    }

    /**
     * Returns the number of likes the buyer gave. To give likes, the buyer can like a product, follow another buyer,
     * like a seller and/or like another buyer's evaluation.
     *
     * @return  int, number of likes given
     */
    public int getLikesGiven() {
        return likesGiven;
    }

    /**
     * Returns the number of evaluations the buyer made.
     *
     * @return  int, number of evaluations made
     */
    public int getEvaluationsMade() {
        return evaluationsMade;
    }

    /**
     * Returns the overall average note of all notes the buyer gave on products.
     *
     * @return  float, overall average note given
     */
    public float getAverageNoteGiven() {
        return averageNoteGiven;
    }

    /**
     * Returns the selected metrics to display on the buyer's profile.
     * @return  ArrayList of String, metrics to be displayed
     */
    public ArrayList<String> getSelectedMetrics(){
        return selectedMetrics;
    }

    // SETTERS

    /**
     * Sets the number of orders made.
     *
     * @param ordersMade    int, number of orders made
     */
    public void setOrdersMade(int ordersMade) {
        this.ordersMade = ordersMade;
    }

    /**
     * Sets the number of products bought.
     *
     * @param productsBought    int, number of products bought
     */
    public void setProductsBought(int productsBought) {
        this.productsBought = productsBought;
    }

    /**
     * Sets the number of likes received.
     *
     * @param likesReceived int, number of likes received
     */
    public void setLikesReceived(int likesReceived) {
        this.likesReceived = likesReceived;
    }

    /**
     * Sets the number of likes given.
     *
     * @param likesGiven    int, number of liikes given
     */
    public void setLikesGiven(int likesGiven) {
        this.likesGiven = likesGiven;
    }

    /**
     * Sets the number of evaluations made.
     *
     * @param evaluationsMade   int, number of evaluations made
     */
    public void setEvaluationsMade(int evaluationsMade) {
        this.evaluationsMade = evaluationsMade;
    }

    /**
     * Sets the overall average note given.
     *
     * @param averageNoteGiven  float, average note given
     */
    public void setAverageNoteGiven(float averageNoteGiven) {
        this.averageNoteGiven = averageNoteGiven;
    }

    /**
     * Sets the metrics to be displayed.
     *
     * @param selectedMetrics   ArrayList of String, metrics to be displayed
     */
    public void setSelectedMetrics(ArrayList<String> selectedMetrics) {this.selectedMetrics = selectedMetrics; };

    // UTILITIES

    // experience points ----------------------------------------------------------------------------------------------

    /**
     * Adds points to the buyer's experience points.
     *
     * @param points    int, experience points gained
     */
    public void addExpPoints(int points) {
        this.expPoints += points;
    }

    /**
     * Removed experience points from the buyer's experience points.
     *
     * @param points    int, experience points to remove
     */
    public void removeExpPoints(int points) {
        this.expPoints -= points;
    }

    // buying points --------------------------------------------------------------------------------------------------

    /**
     * Adds buy points to buyer's buy points.
     *
     * @param points    int, buy points gained
     */
    public void addBuyPoints(int points) {
        this.buyPoints += points;
    }

    /**
     * Removes buy points from the buyer's buy points.
     *
     * @param points    int, buy points to remove
     */
    public void removeBuyPoints(int points) {
        this.buyPoints -= points;
    }

    // inputs ---------------------------------------------------------------------------------------------------------

    private int getUserInputAsInteger() {
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

    // evaluations ----------------------------------------------------------------------------------------------------

    /**
     * Updates the overall average note given.
     *
     * @param note  float, note on 5 of a new evaluation made
     */
    public void updateAverageNoteGiven(float note) {
        notesGiven.add(note);
        float sum = 0;
        for (float n : notesGiven) {
            sum += n;
        }
        averageNoteGiven = sum / notesGiven.size();
    }

    /**
     * Removes a note from the average note given.
     *
     * @param note float, note on 5 of an evaluation to be removed
     */
    public void removeAverageNoteGiven(float note) {
        notesGiven.remove(note);
        float sum = 0;
        for (float n : notesGiven) {
            sum += n;
        }
        averageNoteGiven = sum / notesGiven.size();
    }

    // configuration --------------------------------------------------------------------------------------------------

    /**
     * Configures the metrics to be displayed on the buyer's profile, and updates the selected metrics.
     */
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

    // to string ------------------------------------------------------------------------------------------------------

    /**
     * Returns a string containing the buyer's metrics.
     *
     * @return  String, summary of buyer's metrics
     */
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

    /**
     * Returns a string, containing BuyerMetrics' attributes.
     *
     * @return String, attributes of BuyerMetrics
     */
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
