package cfwz.skiti.go4lunch;

import org.junit.Before;
import org.junit.Test;

import cfwz.skiti.go4lunch.model.Booking;

import static org.junit.Assert.*;


public class BookingTest {
    private Booking booking;


    @Before
    public void setUp() {
        booking = new Booking("18/03/2020","1234", "4321","Restaurant_A");
    }

    @Test
    public void getBookingInfo(){
        assertEquals("18/03/2020", booking.getBookingDate());
        assertEquals("1234", booking.getUserId());
        assertEquals("4321", booking.getRestaurantId());
        assertEquals("Restaurant_A", booking.getRestaurantName());
    }

    @Test
    public void setBookingInfo() {
        booking.setBookingDate("09/09/2020");
        booking.setUserId("1111");
        booking.setRestaurantId("9999");
        booking.setRestaurantName("Restaurant_B");

        assertEquals("09/09/2020", booking.getBookingDate());
        assertEquals("1111", booking.getUserId());
        assertEquals("9999", booking.getRestaurantId());
        assertEquals("Restaurant_B", booking.getRestaurantName());
    }
}