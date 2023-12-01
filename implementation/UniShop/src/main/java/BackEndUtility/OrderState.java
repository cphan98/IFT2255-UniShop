package BackEndUtility;

public enum OrderState implements java.io.Serializable {
    PENDING,
    ACCEPTED,
    REJECTED,
    IN_PRODUCTION,
    IN_DELIVERY,
    DELIVERED,
    CANCELLED,
    RESHIPMENT_IN_DELIVERY,
    RESHIPMENT_DELIVERED,
    RESHIPMENT_CANCELLED
}
