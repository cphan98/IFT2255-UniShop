package products;

public class IssueQuery {
    // attributes
    private String id;
    private String issueDescription;
    private String solutionDescription;
    private int reshipmentTrackingNum;
    private Boolean reshipmentReceived = false;
    private Product replacementProduct;
    private int replacementTrackingNum;
    private Boolean replacementReceived = false;

    // getters
    public String getId() { return id; }
    public String getIssueDescription() { return issueDescription; }
    public String getSolutionDescription() { return solutionDescription; }
    public int getReshipmentTrackingNum() { return reshipmentTrackingNum; }
    public Boolean getReshipmentReceived() { return reshipmentReceived; }
    public Product getReplacementProduct() { return replacementProduct; }
    public int getReplacementTrackingNum() { return replacementTrackingNum; }
    public Boolean getReplacementReceived() { return replacementReceived; }

    // setters
    public void setId(String id) { this.id = id; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setSolutionDescription(String solutionDescription) { this.solutionDescription = solutionDescription; }
    public void setReshipmentTrackingNum(int reshipmentTrackingNum) { this.reshipmentTrackingNum = reshipmentTrackingNum; }
    public void setReshipmentReceived(Boolean reshipmentReceived) { this.reshipmentReceived = reshipmentReceived; }
    public void setReplacementProduct(Product replacementProduct) { this.replacementProduct = replacementProduct; }
    public void setReplacementTrackingNum(int replacementTrackingNum) { this.replacementTrackingNum = replacementTrackingNum; }
    public void setReplacementReceived(Boolean replacementReceived) { this.replacementReceived = replacementReceived; }

    // constructor
    public IssueQuery(String issueDescription) {
        this.id = makeId();
        this.issueDescription = issueDescription;
    }

    // operations
    int idCount = 0;
    private String makeId() {
        int zeros = 3 - Integer.toString(idCount).length();
        return("issue" + ("0".repeat(zeros)) + idCount);
    }
    // TODO: interaction between buyer and seller
}
