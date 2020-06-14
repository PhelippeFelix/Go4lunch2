package cfwz.skiti.go4lunch.stream;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

import cfwz.skiti.go4lunch.model.AutoComplete.AutoCompleteResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Skiti on 12/05/2020
 */

public class GoogleAutoCompleteCalls {
    static String apiKey = "AIzaSyBm5KR0R5LIAKLlQZoiodV6rbQ61iClmL4";
    static int radius = 1500;
    static String types = "establishment";
    static String language = "fr";


    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable AutoCompleteResult autoCompleteResult);
        void onFailure();
    }

    // 2 - Public method to start fetching users following by Jake Wharton
    public static void fetchAutoCompleteResult(GoogleAutoCompleteCalls.Callbacks callbacks, String input, String location){

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<GoogleAutoCompleteCalls.Callbacks> callbacksWeakReference = new WeakReference<GoogleAutoCompleteCalls.Callbacks>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GoogleAutoComplete googleAutoComplete = GoogleAutoComplete.retrofit.create(GoogleAutoComplete.class);

        // 2.3 - Create the call on Github API
        Call<AutoCompleteResult> call = googleAutoComplete.getAutoComplete(input,types,language,location,radius,apiKey);
        // 2.4 - Start the call
        call.enqueue(new Callback<AutoCompleteResult>() {

            @Override
            public void onResponse(Call<AutoCompleteResult> call, Response<AutoCompleteResult> response) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<AutoCompleteResult> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}