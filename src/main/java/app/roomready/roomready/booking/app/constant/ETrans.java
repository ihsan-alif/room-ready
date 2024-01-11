package app.roomready.roomready.booking.app.constant;

public enum ETrans {

    DECLINE("Decline"),

    PENDING("Pending"),

    ACCEPT("Accept");

    private final String displayValue;

    ETrans(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
