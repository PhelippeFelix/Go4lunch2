package cfwz.skiti.go4lunch.api;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import cfwz.skiti.go4lunch.BuildConfig;
import cfwz.skiti.go4lunch.model.googleplaces.PlaceDetails;
import cfwz.skiti.go4lunch.model.googleplaces.ResultDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GooglePlaceDetailsCalls {
     static String apiKey = BuildConfig.google_api_key;

    public interface Callbacks {
        void onResponse(@Nullable ResultDetails resultDetails);
        void onFailure();
    }

    public static void fetchPlaceDetails(Callbacks callbacks, String place_id){

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        GooglePlaceDetails googlePlaceDetails = GooglePlaceDetails.retrofit.create(GooglePlaceDetails.class);

        Call<PlaceDetails> call = googlePlaceDetails.getDetails(place_id,apiKey);
        call.enqueue(new Callback<PlaceDetails>() {

            @Override
            public void onResponse(Call<PlaceDetails> call, Response<PlaceDetails> response) {
                if (callbacksWeakReference.get() != null) {
                    PlaceDetails placeDetails = response.body();
                    ResultDetails resultDetails = placeDetails.getResultDetails();
                    callbacksWeakReference.get().onResponse(resultDetails);}
            }

            @Override
            public void onFailure(Call<PlaceDetails> call, Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}