package cfwz.skiti.go4lunch.stream;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cfwz.skiti.go4lunch.models.Restaurant;
import retrofit2.http.Url;

public class GooglePlace {
    List<Restaurant> listRestaurant = new ArrayList<>();
    String ApiKey = "AIzaSyBm5KR0R5LIAKLlQZoiodV6rbQ61iClmL4";
    private Restaurant restaurant;



    public Restaurant getRestaurant(String placeId) {
        Url url =("https://maps.googleapis.com/maps/api/place/details/json?place_id="+placeId+"=name,formatted_address,periods[],website,,rating,formatted_phone_number&key="+ApiKey);

        return restaurant; }


    public Restaurant JsonToRestaurant (String Json){
        Gson g = new Gson();
        Restaurant restaurant = g.fromJson(Json,Restaurant.class);
        return restaurant; }


    public String JsonRequest (String placeId){


    }
}
