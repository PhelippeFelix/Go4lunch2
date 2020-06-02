package cfwz.skiti.go4lunch.stream;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cfwz.skiti.go4lunch.model.AutoComplete.AutoCompleteResult;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultDetails;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultSearch;

public class GoogleApi  implements GooglePlaceDetailsCalls.Callbacks, GoogleAutoCompleteCalls.Callbacks {
     public AutoCompleteResult mAutoCompleteResult = new AutoCompleteResult();
     public ResultDetails mResultDetails = new ResultDetails();
     public List<ResultSearch> mResultSearchList = new ArrayList<>();
    private int sessiontoken = 1234;


    public ResultDetails getPlaceDetails(String placeId) {
        GooglePlaceDetailsCalls.fetchPlaceDetails(this, placeId);
        return mResultDetails;
    }

    public List<ResultSearch> getResultSearchList(String location) {
        //GooglePlaceSearchCalls.fetchNearbyRestaurants(this, location);
        System.out.println(mResultSearchList);
        return mResultSearchList;
    }

    public AutoCompleteResult getAutoCompleteResult(String location, String input) {
        GoogleAutoCompleteCalls.fetchAutoCompleteResult(this, input,sessiontoken,location);

        return mAutoCompleteResult;
    }


    @Override
    public void onResponse(@Nullable AutoCompleteResult autoCompleteResult) {
        this.setAutoCompleteResult(autoCompleteResult);
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        this.setResultDetails(resultDetails);
    }

    //@Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
        System.out.println(resultSearchList);
        this.setResultSearchList(resultSearchList);
    }

    @Override
    public void onFailure() {
        System.out.println("0000000000000000000000000000");

    }

    public void setAutoCompleteResult(AutoCompleteResult autoCompleteResult) {
        mAutoCompleteResult = autoCompleteResult;
    }

    public void setResultDetails(ResultDetails resultDetails) {
        mResultDetails = resultDetails;
    }

    public void setResultSearchList(List<ResultSearch> resultSearchList) {
        System.out.println(resultSearchList);
        this.mResultSearchList = resultSearchList;
    }

    public List<ResultSearch> getResultSearchList2() {
        System.out.println(mResultSearchList);
        return mResultSearchList;
    }
}
