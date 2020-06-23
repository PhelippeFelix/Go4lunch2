package cfwz.skiti.go4lunch.api;

import cfwz.skiti.go4lunch.model.autocomplete.AutoCompleteResult;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GoogleAutoComplete {
    String BASE_URL = "https://maps.googleapis.com";

    @GET("/maps/api/place/autocomplete/json")
    Call<AutoCompleteResult> getAutoComplete(@Query("input") String input,
                                             @Query("types") String types,
                                             @Query("language") String language,
                                             @Query("location") String location,
                                             @Query("radius") int radius,
                                             @Query("key") String key);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
