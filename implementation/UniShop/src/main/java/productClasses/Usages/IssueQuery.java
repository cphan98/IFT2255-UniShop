package productClasses.Usages;

import BackEndUtility.InputManager;
import productClasses.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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

    public IssueQuery(String issueDescription) {
        this.id = "";
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.requestDate = today.format(formatter);
        this.issueDescription = issueDescription;
        this.reshipmentTrackingNum = "";
    }

    // GETTERS

    public String getId() { return id; }
    public String getIssueDescription() { return issueDescription; }
    public String getSolutionDescription() { return solutionDescription; }
    public String getReshipmentTrackingNum() { return reshipmentTrackingNum; }
    public Boolean getReshipmentReceived() { return reshipmentReceived; }
    public HashMap<Product, Integer> getReplacementProduct() { return replacementProducts; }
    public String getReplacementTrackingNum() { return replacementTrackingNum; }
    public Boolean getReplacementReceived() { return replacementReceived; }
    public HashMap<Product, Integer> getReshipmentProducts() { return reshipmentProducts; }
    public String getRequestDate() { return requestDate; }
    public Order getReplacementOrder() { return replacementOrder; }

    // SETTERS

    public void setId(String id) { this.id = id; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setSolutionDescription(String solutionDescription) { this.solutionDescription = solutionDescription; }
    public void setReshipmentTrackingNum(String reshipmentTrackingNum) { this.reshipmentTrackingNum = reshipmentTrackingNum; }
    public void setReshipmentReceived(Boolean reshipmentReceived) { this.reshipmentReceived = reshipmentReceived; }
    public void setReplacementProducts(HashMap<Product, Integer> replacementProducts) { this.replacementProducts = replacementProducts; }
    public void setReplacementTrackingNum(String replacementTrackingNum) { this.replacementTrackingNum = replacementTrackingNum; }
    public void setReplacementReceived(Boolean replacementReceived) { this.replacementReceived = replacementReceived; }
    public void setReshipmentProducts(HashMap<Product, Integer> reshipmentProducts) { this.reshipmentProducts = reshipmentProducts; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
    public void setReplacementOrder(Order replacementOrder) { this.replacementOrder = replacementOrder; }

    // UTILITIES

    // id -------------------------------------------------------------------------------------------------------------

    public String makeId(int idCount) {
        int zeros = 3 - Integer.toString(idCount).length();
        return ("issue" + ("0".repeat(zeros)) + idCount);
    }

    // solution -------------------------------------------------------------------------------------------------------

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
