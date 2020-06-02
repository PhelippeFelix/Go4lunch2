package cfwz.skiti.go4lunch.model;

/**
 * Created by Skiti on 02/06/2020
 */
public class Booking {
    private String bookingDate;
    private String workmateUid;
    private String restaurantPlaceId;
    private String restaurantName;

    public Booking() {}

    public Booking(String bookingDate, String workmateUid, String restaurantPlaceId, String restaurantName) {
        this.bookingDate = bookingDate;
        this.workmateUid = workmateUid;
        this.restaurantPlaceId = restaurantPlaceId;
        this.restaurantName = restaurantName; }


    // GETTER
    public String getBookingDate() {
        return bookingDate;
    }
    public String getUserId() {
        return workmateUid;
    }
    public String getRestaurantName() { return restaurantName; }
    public String getRestaurantId() {
        return restaurantPlaceId;
    }

    // SETTER
    public void setRestaurantId(String restaurantId) {
        this.restaurantPlaceId = restaurantId;
    }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public void setUserId(String userId) { this.workmateUid = userId; }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
