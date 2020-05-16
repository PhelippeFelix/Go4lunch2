package cfwz.skiti.go4lunch.stream;


import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import cfwz.skiti.go4lunch.models.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Skiti on 12/05/2020
 */

public class GooglePlaceSearchCalls {
    static String apiKey = "AIzaSyBm5KR0R5LIAKLlQZoiodV6rbQ61iClmL4";
    static int radius = 1500;
    static String type = "restaurant";


        // 1 - Creating a callback
        public interface Callbacks {
            void onResponse(@Nullable List<Restaurant> restaurantList);
            void onFailure();
        }

        // 2 - Public method to start fetching users following by Jake Wharton
        public static void fetchNearbyRestaurants(Callbacks callbacks, String location){

            // 2.1 - Create a weak reference to callback (avoid memory leaks)
            final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

            // 2.2 - Get a Retrofit instance and the related endpoints
            GooglePlaceSearch googlePlaceSearch = GooglePlaceSearch.retrofit.create(GooglePlaceSearch.class);

            // 2.3 - Create the call on Github API
            Call<List<Restaurant>> call = googlePlaceSearch.getNearbyRestaurants(location,radius,type,apiKey);
            // 2.4 - Start the call
            call.enqueue(new Callback<List<Restaurant>>() {

                @Override
                public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                    // 2.5 - Call the proper callback used in controller (MainFragment)
                    if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
                }

                @Override
                public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                    // 2.5 - Call the proper callback used in controller (MainFragment)
                    if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
                }
            });
        }
    }

