package Metrics;

import BackEndUtility.InputManager;

import java.util.ArrayList;
/**
 * The SellerMetrics class represents the metrics of a seller in UniShop.
 * It tracks various metrics such as revenue, products sold, average note received, and likes.
 *
 */
public class SellerMetrics implements Metrics, java.io.Serializable {

    // ATTRIBUTES

    private float revenue;
    private int productsSold;
    private float averageNoteReceived;
    private int likes;
    protected ArrayList<Float> notesReceived;
    public ArrayList<String> selectedMetrics;//to be shown on the users profile

    // CONSTRUCTOR
    /**
     * Constructs a SellerMetrics object with initial metric values for the seller.
     */
    public SellerMetrics() {
        revenue = 0;
        productsSold = 0;
        averageNoteReceived = 0;
        notesReceived = new ArrayList<>();
        selectedMetrics = new ArrayList<>();
    }

    // GETTERS
    /**
     * Retrieves the list of selected metrics to display in the profile.
     *
     * @return The list of selected metrics by the user.
     */
    public ArrayList<String> getSelectedMetrics(){
        return selectedMetrics;
    }
    /**
     * Retrieves the revenue for the seller.
     *
     * @return The revenue metric.
     */
    public float getRevenue() {
        return revenue;
    }
    /**
     * Retrieves number of the products sold by the seller.
     *
     * @return The products sold metric.
     */
    public int getProductsSold() {
        return productsSold;
    }
    /**
     * Retrieves the average note received by the seller.
     *
     * @return The average note received metric.
     */
    public float getAverageNoteReceived() {
        return averageNoteReceived;
    }
    /**
     * Retrieves the likes that the seller has obtained.
     *
     * @return The likes metric.
     */
    public int getLikes() {
        return likes;
    }

    // SETTERS
    /**
     * Sets the list of selected metrics.
     *
     * @param selectedMetrics The new list of selected metrics.
     */
    public void setSelectedMetrics(ArrayList<String> selectedMetrics) {this.selectedMetrics = selectedMetrics; };

    // UTILITIES

    // revenue --------------------------------------------------------------------------------------------------------
    /**
     * Updates the revenue metric.
     *
     * @param revenue The new revenue value for the seller.
     */
    public void updateRevenue(float revenue) {
        this.revenue = revenue;
    }

    // products -------------------------------------------------------------------------------------------------------
    /**
     * Updates the number of products sold metric.
     *
     * @param productsSold The new value for products sold.
     */
    public void updateProductsSold(int productsSold) {
        this.productsSold = productsSold;
    }

    // evaluations ----------------------------------------------------------------------------------------------------
    /**
     * Updates the average note received metric.
     *
     * @param note The new note value to be added to the notesReceived list.
     */
    public void updateAverageNoteReceived(float note) {
        notesReceived.add(note);
        float sum = 0;
        for (float n : notesReceived) {
            sum += n;
        }
        averageNoteReceived = Math.round((sum / notesReceived.size()) * 10) / 10f;
    }
    /**
     * Removes a note from the notesReceived list and updates the average note received metric.
     *
     * @param note The note value to be removed from the notesReceived list.
     */
    public void removeAverageNoteReceived(float note) {
        notesReceived.remove(note);
        float sum = 0;
        for (float n : notesReceived) {
            sum += n;
        }
        averageNoteReceived = Math.round((sum / notesReceived.size()) * 10) / 10f;
    }

    // likes ----------------------------------------------------------------------------------------------------------
    /**
     * Updates the likes metric.
     *
     * @param likes The new value for likes.
     */
    public void updateLikes(int likes) {
        this.likes = likes;
    }

    // configuration --------------------------------------------------------------------------------------------------
    /**
     * Configures the metrics, allowing the seller to select metrics to be displayed on their profile.
     */
    public void configureMetrics(){
        ArrayList<String> allMetrics = new ArrayList<>();
        ArrayList<Number> allMetricsValues =new ArrayList<>();
        allMetrics.add("Income");
        allMetricsValues.add(revenue);
        allMetrics.add("Products sold" );
        allMetricsValues.add(productsSold);
        allMetrics.add("Average note received" );
        allMetricsValues.add(averageNoteReceived);
        allMetrics.add("Total of likes received on products" );
        allMetricsValues.add(likes);

        ArrayList<String> selectedMetrics = selectMetrics(allMetrics, allMetricsValues);
    }

    private ArrayList<String> selectMetrics(ArrayList<String> allMetrics, ArrayList<Number> allMetricsValues){
        System.out.println("Select metrics to be displayed in your profile (3 max.)");
        System.out.println("Enter the number corresponding to the metric to select or '0' to finish:");
        int count = 1;
        for (String metric : allMetrics){
            System.out.println(count + ". " + metric);
            count++;
        }
        boolean continueLoop = true;
        while (continueLoop){
            int choice = getUserInputAsInteger();
            if (choice == 0){
                break;
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
                        break;
                    }
                } else {
                    System.out.println("Metric already displayed in profile");
                }
            } else {
                System.out.println("invalid selection");
            }
        }
        return selectedMetrics;
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

    // to string ------------------------------------------------------------------------------------------------------
    /**
     * Returns a string representation of selected metrics.
     *
     * @return A string representing selected metrics.
     */
    public String SomeMetricsToString(){
        return"Products sold: " + productsSold + "\n" +
                "Average note received: "  + averageNoteReceived + "\n";
    }
    /**
     * Returns a string representation of all metrics.
     *
     * @return A string representing all metrics.
     */
    public String AlltoString() {
        return "Since inception," + "\n" +
            "Income: " + revenue + "$\n" +
            "Products sold: " + productsSold + "\n" +
            "Average note received: " + averageNoteReceived + "\n" +
            "Total of likes received on products: " + likes + "\n";
    }
}
