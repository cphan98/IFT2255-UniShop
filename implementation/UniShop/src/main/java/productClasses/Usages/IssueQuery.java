package productClasses.Usages;

import productClasses.Product;

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
    private HashMap<Product, Integer> replacementProducts;
    private String replacementTrackingNum;
    private Boolean replacementReceived = false;

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

    // OPERATIONS
}
