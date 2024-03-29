package BackEndUtility;

/**
 * Enum class for the states of the orders
 */
public enum OrderState implements java.io.Serializable {
    PENDING,
    REJECTED,
    IN_PRODUCTION,
    IN_DELIVERY,
    DELIVERED,
    CANCELLED,
    RESHIPMENT_IN_DELIVERY,
    RESHIPMENT_DELIVERED,
    RESHIPMENT_CANCELLED,
    REPLACEMENT_IN_PRODUCTION,
    REPLACEMENT_IN_DELIVERY,
    REPLACEMENT_DELIVERED
}
