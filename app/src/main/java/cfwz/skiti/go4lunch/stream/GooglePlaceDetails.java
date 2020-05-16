package cfwz.skiti.go4lunch.stream;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

import cfwz.skiti.go4lunch.models.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    Call<Restaurant> getDetails(@Query("place_id") String place_id,
                                @Query("fields") String fields,
                                @Query("key") String key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
