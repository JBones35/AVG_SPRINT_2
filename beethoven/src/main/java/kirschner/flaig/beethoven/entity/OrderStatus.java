package kirschner.flaig.beethoven.entity;

public enum OrderStatus {
    SHIPPED("Shipped"),
    CANCELLED("Cancelled"),
    PROCESSED("Processed");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
