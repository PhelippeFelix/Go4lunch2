package cfwz.skiti.go4lunch.api;

import android.os.Build;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import cfwz.skiti.go4lunch.BuildConfig;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.autocomplete.AutoCompleteResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoogleAutoCompleteCalls {
    static String apiKey = BuildConfig.api_key;
    static int radius = 1500;
    static String types = "establishment";
    static String language = "fr";

    public interface Callbacks {
        void onResponse(@Nullable AutoCompleteResult autoCompleteResult);
        void onFailure();
    }

    public static void fetchAutoCompleteResult(GoogleAutoCompleteCalls.Callbacks callbacks, String input, String location){

        final WeakReference<GoogleAutoCompleteCalls.Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        GoogleAutoComplete googleAutoComplete = GoogleAutoComplete.retrofit.create(GoogleAutoComplete.class);

        Call<AutoCompleteResult> call = googleAutoComplete.getAutoComplete(input,types,language,location,radius,apiKey);
        call.enqueue(new Callback<AutoCompleteResult>() {

            @Override
            public void onResponse(Call<AutoCompleteResult> call, Response<AutoCompleteResult> response) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<AutoCompleteResult> call, Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}