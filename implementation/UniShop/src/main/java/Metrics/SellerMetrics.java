package Metrics;

import BackEndUtility.InputManager;

import java.util.ArrayList;

public class SellerMetrics implements Metrics, java.io.Serializable {

    // ATTRIBUTES

    private float revenue;
    private int productsSold;
    private float averageNoteReceived;
    private int likes;
    protected ArrayList<Float> notesReceived;
    public ArrayList<String> selectedMetrics;//to be shown on the users profile

    // CONSTRUCTOR

    public SellerMetrics() {
        revenue = 0;
        productsSold = 0;
        averageNoteReceived = 0;
        notesReceived = new ArrayList<>();
        selectedMetrics = new ArrayList<>();
    }

    // GETTERS

    public ArrayList<String> getSelectedMetrics(){
        return selectedMetrics;
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

    // SETTERS

    public void setSelectedMetrics(ArrayList<String> selectedMetrics) {this.selectedMetrics = selectedMetrics; };

    // UTILITIES

    // revenue

    public void updateRevenue(float revenue) {
        this.revenue = revenue;
    }

    // products

    public void updateProductsSold(int productsSold) {
        this.productsSold = productsSold;
    }

    // evaluations

    public void updateAverageNoteReceived(float note) {
        notesReceived.add(note);
        float sum = 0;
        for (float n : notesReceived) {
            sum += n;
        }
        averageNoteReceived = Math.round((sum / notesReceived.size()) * 10) / 10f;
    }

    public void removeAverageNoteReceived(float note) {
        notesReceived.remove(note);
        float sum = 0;
        for (float n : notesReceived) {
            sum += n;
        }
        averageNoteReceived = Math.round((sum / notesReceived.size()) * 10) / 10f;
    }

    // likes

    public void updateLikes(int likes) {
        this.likes = likes;
    }

    // configuration

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

    // inputs

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

    // to string

    public String SomeMetricsToString(){
        return"Products sold: " + productsSold + "\n" +
                "Average note received: "  + averageNoteReceived + "\n";
    }

    public String AlltoString() {
        return "Since inception," + "\n" +
            "Income: " + revenue + "$\n" +
            "Products sold: " + productsSold + "\n" +
            "Average note received: " + averageNoteReceived + "\n" +
            "Total of likes received on products: " + likes + "\n";
    }
}
