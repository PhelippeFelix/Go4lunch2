package cfwz.skiti.go4lunch.stream;


import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;

import java.lang.ref.WeakReference;
import java.util.List;

import cfwz.skiti.go4lunch.model.GooglePlaces.ResultSearch;
import cfwz.skiti.go4lunch.model.GooglePlaces.SearchPlace;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Skiti on 12/05/2020
 */

public class GooglePlaceSearchCalls {
    static String apiKey = "AIzaSyBm5KR0R5LIAKLlQZoiodV6rbQ61iClmL4";
    static String type = "restaurant";
    static String rankby = "distance";


        // 1 - Creating a callback
        public interface Callbacks {
            void onResponse(@Nullable List<ResultSearch> resultSearchList);
            void onFailure();
        }

        // 2 - Public method to start fetching users following by Jake Wharton
        public static void fetchNearbyRestaurants(Callbacks callbacks, String location){

            // 2.1 - Create a weak reference to callback (avoid memory leaks)
            final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

            // 2.2 - Get a Retrofit instance and the related endpoints
            GooglePlaceSearch googlePlaceSearch = GooglePlaceSearch.retrofit.create(GooglePlaceSearch.class);

            // 2.3 - Create the call on Github API
            Call<SearchPlace> call = googlePlaceSearch.getNearbyRestaurants(location,rankby,type,apiKey);
            // 2.4 - Start the call
            call.enqueue(new Callback<SearchPlace>() {

                @Override
                public void onResponse(Call<SearchPlace> call, Response<SearchPlace> response) {
                    // 2.5 - Call the proper callback used in controller (MainFragment)
                    if (callbacksWeakReference.get() != null) {
                        SearchPlace searchPlace = response.body();
                        List<ResultSearch> resultSearchList = searchPlace.getResultSearches();
                        callbacksWeakReference.get().onResponse(resultSearchList);}
                }

                @Override
                public void onFailure(Call<SearchPlace> call, Throwable t) {
                    // 2.5 - Call the proper callback used in controller (MainFragment)
                    System.out.println(t.toString());
                    if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
                }
            });
        }
    }

