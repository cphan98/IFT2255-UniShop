package products;

public class IssueQuery {
    // attributes
    private String issueDescription;
    private String solutionDescription;
    private int reshipmentTrackingNum;
    private Boolean reshipmentReceived;
    private Product replacementProduct;
    private int replacementTrackingNum;
    private Boolean replacementReceived;

    // getters
    public String getIssueDescription() { return issueDescription; }
    public String getSolutionDescription() { return solutionDescription; }
    public int getReshipmentTrackingNum() { return reshipmentTrackingNum; }
    public Boolean getReshipmentReceived() { return reshipmentReceived; }
    public Product getReplacementProduct() { return replacementProduct; }
    public int getReplacementTrackingNum() { return replacementTrackingNum; }
    public Boolean getReplacementReceived() { return replacementReceived; }

    // setters
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setSolutionDescription(String solutionDescription) { this.solutionDescription = solutionDescription; }
    public void setReshipmentTrackingNum(int reshipmentTrackingNum) { this.reshipmentTrackingNum = reshipmentTrackingNum; }
    public void setReshipmentReceived(Boolean reshipmentReceived) { this.reshipmentReceived = reshipmentReceived; }
    public void setReplacementProduct(Product replacementProduct) { this.replacementProduct = replacementProduct; }
    public void setReplacementTrackingNum(int replacementTrackingNum) { this.replacementTrackingNum = replacementTrackingNum; }
    public void setReplacementReceived(Boolean replacementReceived) { this.replacementReceived = replacementReceived; }
}
