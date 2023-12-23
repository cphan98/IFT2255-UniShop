package productClasses.Usages;

import BackEndUtility.InputManager;
import productClasses.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Class that represents an issue query, which is a request from a buyer to the seller to solve an issue with a product
 */
public class IssueQuery implements java.io.Serializable {

    // ATTRIBUTES

    private String id;
    private String requestDate;
    private String issueDescription;
    private String solutionDescription;
    private HashMap<Product, Integer> reshipmentProducts = new HashMap<>();
    private String reshipmentTrackingNum;
    private Boolean reshipmentReceived = false;
    private HashMap<Product, Integer> replacementProducts = new HashMap<>();
    private Order replacementOrder;
    private String replacementTrackingNum;
    private Boolean replacementReceived = false;

    // CONSTRUCTOR

    /**
     * Constructor of the class IssueQuery
     *
     * @param issueDescription the description of the issue
     */
    public IssueQuery(String issueDescription) {
        this.id = "";
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.requestDate = today.format(formatter);
        this.issueDescription = issueDescription;
        this.reshipmentTrackingNum = "";
    }

    // GETTERS

    /**
     * Method that returns the id of the issue query
     * @return the id of the issue query
     */
    public String getId() { return id; }

    /**
     * Method that returns the description of the issue
     * @return the description of the issue
     */
    public String getIssueDescription() { return issueDescription; }

    /**
     * Method that returns the description of the solution
     * @return the description of the solution
     */
    public String getSolutionDescription() { return solutionDescription; }

    /**
     * Method that returns the tracking number of the reshipment
     * @return the tracking number of the reshipment
     */
    public String getReshipmentTrackingNum() { return reshipmentTrackingNum; }

    /**
     * Method that returns the status of the reshipment
     * @return true if the reshipment has been received, false otherwise
     */
    public Boolean getReshipmentReceived() { return reshipmentReceived; }

    /**
     * Method that returns the replacement products and their quantities
     * @return the replacement products and their quantities
     */
    public HashMap<Product, Integer> getReplacementProduct() { return replacementProducts; }

    /**
     * Method that returns the tracking number of the replacement
     * @return the tracking number of the replacement
     */
    public String getReplacementTrackingNum() { return replacementTrackingNum; }

    /**
     * Method that returns the status of the replacement
     * @return true if the replacement has been received, false otherwise
     */
    public Boolean getReplacementReceived() { return replacementReceived; }

    /**
     * Method that returns the products to be reshipped and their quantities
     * @return the products to be reshipped and their quantities
     */
    public HashMap<Product, Integer> getReshipmentProducts() { return reshipmentProducts; }

    /**
     * Method that returns the date of the request
     * @return the date of the request
     */
    public String getRequestDate() { return requestDate; }

    /**
     * Method that returns the replacement order
     * @return the replacement order
     */
    public Order getReplacementOrder() { return replacementOrder; }

    // SETTERS

    /**
     * Method that sets the id of the issue query
     * @param id the new id of the issue query
     */
    public void setId(String id) { this.id = id; }

    /**
     * Method that sets the description of the issue
     * @param issueDescription the new description of the issue
     */
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    /**
     * Method that sets the description of the solution
     * @param solutionDescription the new description of the solution
     */
    public void setSolutionDescription(String solutionDescription) { this.solutionDescription = solutionDescription; }

    /**
     * Method that sets the tracking number of the reshipment
     * @param reshipmentTrackingNum the new tracking number of the reshipment
     */
    public void setReshipmentTrackingNum(String reshipmentTrackingNum) { this.reshipmentTrackingNum = reshipmentTrackingNum; }

    /**
     * Method that sets the status of the reshipment
     * @param reshipmentReceived the new status of the reshipment
     */
    public void setReshipmentReceived(Boolean reshipmentReceived) { this.reshipmentReceived = reshipmentReceived; }

    /**
     * Method that sets the replacement products and their quantities
     * @param replacementProducts the new replacement products and their quantities
     */
    public void setReplacementProducts(HashMap<Product, Integer> replacementProducts) { this.replacementProducts = replacementProducts; }

    /**
     * Method that sets the tracking number of the replacement
     * @param replacementTrackingNum the new tracking number of the replacement
     */
    public void setReplacementTrackingNum(String replacementTrackingNum) { this.replacementTrackingNum = replacementTrackingNum; }

    /**
     * Method that sets the status of the replacement
     * @param replacementReceived the new status of the replacement
     */
    public void setReplacementReceived(Boolean replacementReceived) { this.replacementReceived = replacementReceived; }

    /**
     * Method that sets the products to be reshipped and their quantities
     * @param reshipmentProducts the new products to be reshipped and their quantities
     */
    public void setReshipmentProducts(HashMap<Product, Integer> reshipmentProducts) { this.reshipmentProducts = reshipmentProducts; }

    /**
     * Method that sets the date of the request
     * @param requestDate the new date of the request
     */
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    /**
     * Method that sets the replacement order
     * @param replacementOrder the new replacement order
     */
    public void setReplacementOrder(Order replacementOrder) { this.replacementOrder = replacementOrder; }

    // UTILITIES

    // id -------------------------------------------------------------------------------------------------------------

    /**
     * Method that creates an id for the issue query
     * @param idCount the number of issue queries that have been created so far
     * @return the id of the issue query
     */
    public String makeId(int idCount) {
        int zeros = 3 - Integer.toString(idCount).length();
        return ("issue" + ("0".repeat(zeros)) + idCount);
    }

    // solution -------------------------------------------------------------------------------------------------------

    /**
     * Method that proposes a solution to the buyer by setting the solution description to his choice
     */
    public void proposeSolution() {
        System.out.println("Which solution do you want to propose to the buyer?");
        System.out.println("1. Repair the defective product");
        System.out.println("2. Reshipment of a replacement product");
        System.out.println("3. Return");
        int choice2 = getUserInputAsInteger();
        switch (choice2){
            case 1:
                setSolutionDescription("Repair the defective product");
                break;
            case 2:
                setSolutionDescription("Reshipment of a replacement product");
                break;
            }
        System.out.println("The buyer will be notified shortly to approve this solution ");
            // method to notify the buyer and accept the solution

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
}
