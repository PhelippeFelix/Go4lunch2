package cfwz.skiti.go4lunch.stream;

import cfwz.skiti.go4lunch.model.GooglePlaces.SearchPlace;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Skiti on 12/05/2020
 */

public interface GooglePlaceSearch {
    String BASE_URL = "https://maps.googleapis.com";

    @GET("/maps/api/place/nearbysearch/json")
    Call<SearchPlace> getNearbyRestaurants(@Query("location") String location,
                                                  @Query("rankby") String rankby,
                                                  @Query("type") String type,
                                                  @Query("key") String key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
