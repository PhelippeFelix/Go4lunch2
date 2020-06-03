package cfwz.skiti.go4lunch.stream;

import cfwz.skiti.go4lunch.model.GooglePlaces.PlaceDetails;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Skiti on 12/05/2020
 */

public interface GooglePlaceDetails {
    String BASE_URL = "https://maps.googleapis.com";

    @GET("/maps/api/place/details/json")
    Call<PlaceDetails> getDetails(@Query("place_id") String place_id,
                                  @Query("key") String key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}