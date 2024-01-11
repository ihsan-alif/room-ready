package app.roomready.roomready.booking.app.constant;


public enum ERoom {
    AVAILABLE("available"),
    BOOKED("booked");

    private final String displayValue;

    ERoom(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue(){
        return displayValue;
    }
}
