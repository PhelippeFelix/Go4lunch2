package cfwz.skiti.go4lunch.api;

import cfwz.skiti.go4lunch.model.googleplaces.SearchPlace;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


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
