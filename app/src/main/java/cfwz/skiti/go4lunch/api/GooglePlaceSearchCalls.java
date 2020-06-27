package cfwz.skiti.go4lunch.api;


import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

import cfwz.skiti.go4lunch.BuildConfig;
import cfwz.skiti.go4lunch.model.googleplaces.ResultSearch;
import cfwz.skiti.go4lunch.model.googleplaces.SearchPlace;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GooglePlaceSearchCalls {
    static String apiKey = BuildConfig.google_api_key;
    static String type = "restaurant";
    static String rankby = "distance";

        public interface Callbacks {
            void onResponse(@Nullable List<ResultSearch> resultSearchList);
            void onFailure();
        }

        public static void fetchNearbyRestaurants(Callbacks callbacks, String location){

            final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

            GooglePlaceSearch googlePlaceSearch = GooglePlaceSearch.retrofit.create(GooglePlaceSearch.class);

            Call<SearchPlace> call = googlePlaceSearch.getNearbyRestaurants(location,rankby,type,apiKey);
            call.enqueue(new Callback<SearchPlace>() {

                @Override
                public void onResponse(Call<SearchPlace> call, Response<SearchPlace> response) {
                    if (callbacksWeakReference.get() != null) {
                        SearchPlace searchPlace = response.body();
                        List<ResultSearch> resultSearchList = searchPlace.getResultSearches();
                        callbacksWeakReference.get().onResponse(resultSearchList);}
                }

                @Override
                public void onFailure(Call<SearchPlace> call, Throwable t) {
                    System.out.println(t.toString());
                    if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
                }
            });
        }
    }

