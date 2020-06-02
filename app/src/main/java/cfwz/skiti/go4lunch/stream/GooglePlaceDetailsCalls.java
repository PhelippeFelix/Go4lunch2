package cfwz.skiti.go4lunch.stream;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import cfwz.skiti.go4lunch.model.GooglePlaces.PlaceDetails;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Skiti on 12/05/2020
 */

public class GooglePlaceDetailsCalls {

     static String apiKey = "AIzaSyBm5KR0R5LIAKLlQZoiodV6rbQ61iClmL4";

    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable ResultDetails resultDetails);
        void onFailure();
    }

    // 2 - Public method to start fetching users following by Jake Wharton
    public static void fetchPlaceDetails(Callbacks callbacks, String place_id){

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GooglePlaceDetails googlePlaceDetails = GooglePlaceDetails.retrofit.create(GooglePlaceDetails.class);

        // 2.3 - Create the call on Github API
        Call<PlaceDetails> call = googlePlaceDetails.getDetails(place_id,apiKey);
        // 2.4 - Start the call
        call.enqueue(new Callback<PlaceDetails>() {

            @Override
            public void onResponse(Call<PlaceDetails> call, Response<PlaceDetails> response) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) {
                    PlaceDetails placeDetails = response.body();
                    ResultDetails resultDetails = placeDetails.getResultDetails();
                    callbacksWeakReference.get().onResponse(resultDetails);}
            }

            @Override
            public void onFailure(Call<PlaceDetails> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}