package cfwz.skiti.go4lunch.stream;

import android.location.Location;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cfwz.skiti.go4lunch.models.Restaurant;

import static java.security.AccessController.getContext;

public class GoogleApi  implements GooglePlaceDetailsCalls.Callbacks, GooglePlaceSearchCalls.Callbacks, GoogleAutoCompleteCalls.Callbacks {
    private Restaurant mRestaurant=new Restaurant();
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private int sessiontoken = 1234;


    public Restaurant getRestaurantInfo(String placeId) {
        GooglePlaceDetailsCalls.fetchPlaceDetails(this, placeId);
        return mRestaurant;
    }

    public List<Restaurant> getRestaurantList(String location) {
        GooglePlaceSearchCalls.fetchNearbyRestaurants(this, location);
        return mRestaurantList;
    }

    public List<Restaurant> getAutoCompleteList(String location, String input) {
        GoogleAutoCompleteCalls.fetchAutoCompleteResult(this, input,sessiontoken,location);
        return mRestaurantList;
    }

    @Override
    public void onResponse(@Nullable Restaurant restaurant) {
        if (restaurant != null){
            this.mRestaurant = restaurant;
        }
    }

    @Override
    public void onResponse(@Nullable List<Restaurant> restaurantList) {
        if (restaurantList != null){
            this.mRestaurantList=restaurantList;
        }
    }

    @Override
    public void onFailure() {
        System.out.println("prout");
    }
}
