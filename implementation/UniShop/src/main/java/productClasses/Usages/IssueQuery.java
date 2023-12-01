package productClasses.Usages;

import BackEndUtility.InputManager;
import productClasses.Product;
import UIs.UIUtilities;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class IssueQuery {
    // ATTRIBUTES
    private String id;
    private String requestDate;
    private String issueDescription;
    private String solutionDescription;
    private HashMap<Product, Integer> reshipmentProducts = new HashMap<>();
    private String reshipmentTrackingNum;
    private Boolean reshipmentReceived = false;
    private Product replacementProduct;
    private String replacementTrackingNum;
    private Boolean replacementReceived = false;
    private UIUtilities uiUtilities;

    // GETTERS
    public String getId() { return id; }
    public String getIssueDescription() { return issueDescription; }
    public String getSolutionDescription() { return solutionDescription; }
    public String getReshipmentTrackingNum() { return reshipmentTrackingNum; }
    public Boolean getReshipmentReceived() { return reshipmentReceived; }
    public Product getReplacementProduct() { return replacementProduct; }
    public String getReplacementTrackingNum() { return replacementTrackingNum; }
    public Boolean getReplacementReceived() { return replacementReceived; }
    public HashMap<Product, Integer> getReshipmentProducts() { return reshipmentProducts; }
    public String getRequestDate() { return requestDate; }

    // SETTERS
    public void setId(String id) { this.id = id; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setSolutionDescription(String solutionDescription) { this.solutionDescription = solutionDescription; }
    public void setReshipmentTrackingNum(String reshipmentTrackingNum) { this.reshipmentTrackingNum = reshipmentTrackingNum; }
    public void setReshipmentReceived(Boolean reshipmentReceived) { this.reshipmentReceived = reshipmentReceived; }
    public void setReplacementProduct(Product replacementProduct) { this.replacementProduct = replacementProduct; }
    public void setReplacementTrackingNum(String replacementTrackingNum) { this.replacementTrackingNum = replacementTrackingNum; }
    public void setReplacementReceived(Boolean replacementReceived) { this.replacementReceived = replacementReceived; }
    public void setReshipmentProducts(HashMap<Product, Integer> reshipmentProducts) { this.reshipmentProducts = reshipmentProducts; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    // CONSTRUCTOR
    int idCount = 0;
    private String makeId() {
        int zeros = 3 - Integer.toString(idCount).length();
        String id = ("issue" + ("0".repeat(zeros)) + idCount);
        idCount += 1;
        return id;
    }

    int reshipmentCount = 0;
    private String makeReshipmentTrackingNum() {
        int zeros = 12 - Integer.toString(reshipmentCount).length();
        String trackingNum = ("0".repeat(zeros) + reshipmentCount);
        reshipmentCount += 1;
        return trackingNum;
    }


    public IssueQuery(String issueDescription) {
        this.id = makeId();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.requestDate = today.format(formatter);
        this.issueDescription = issueDescription;
        this.reshipmentTrackingNum = makeReshipmentTrackingNum();
    }
    public void createTicket(){
        boolean continueLoop = true;
        while(continueLoop){
            System.out.println("What is the problem with the order?");
            System.out.println("1. Wrong item received");
            System.out.println("2. Defective product");
            System.out.println("3. Missing product ");
            System.out.println("4. Wrong model of the product ordered ");
            System.out.println("5. Order arrived too late");
            System.out.println("6. No longer need the product");
            System.out.println("7. The product does not match the description");
            System.out.println("8. The product does not meet expectations");
            System.out.println("9. Found a better price elsewhere");
            System.out.println("10. Fraudulent purchase");
            System.out.println("11. Return");

            int choice = getUserInputAsInteger();
            if(choice < 11){
                id =makeId();
            }
            switch (choice){
                case 1:

                    issueDescription = "Wrong item received";
                    break;
                case 2:
                    issueDescription = "Defective product";
                    break;

                case 3:
                    issueDescription = "Missing product";
                    break;

                case 4:
                    issueDescription = "Wrong model of the product ordered";
                    break;

                case 5:
                    issueDescription = "Order arrived too late";
                    break;

                case 6:
                    issueDescription = "No longer need the product";
                    break;

                case 7:
                    issueDescription = "The product does not match the description";
                    break;

                case 8:
                    issueDescription = "The product does not meet expectations";
                    break;

                case 9:
                    issueDescription = "Found a better price elsewhere";
                    break;

                case 10:
                    issueDescription = "Fraudulent purchase";
                    break;


            }
            System.out.println("the seller will be notified shortly");
            break;
        }
    }
    //change this methods name
    public boolean acceptSolution(){
        System.out.println("The seller has proposed:  " + solutionDescription);
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.println("3. Return");
        int choice = getUserInputAsInteger();
            switch (choice){
            case 1:
                if (solutionDescription == "Repair the defective product"){
                    reshipmentTrackingNum = makeReshipmentTrackingNum();
                }
                if (solutionDescription == "Reshipment of a replacement product"){
                    replacementTrackingNum = makeReshipmentTrackingNum();
                }
                System.out.println("Please confirm your choice");
                return true;

                case 2:
                System.out.println("Sorry :( ");
                return false;
        }
        System.out.println("Thanks for accepting the solution!");
         return true;
    }


    public void proposeSolution(){
        boolean continueLooop = true;
        while (continueLooop){
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
            break;
        }
    }
    public int getUserInputAsInteger() {
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

    // OPERATIONS

    // TODO : toString
}
