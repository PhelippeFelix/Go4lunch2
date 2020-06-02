package cfwz.skiti.go4lunch.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultDetails;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultSearch;
import cfwz.skiti.go4lunch.stream.GoogleApi;
import cfwz.skiti.go4lunch.stream.GooglePlaceDetailsCalls;
import cfwz.skiti.go4lunch.stream.GooglePlaceSearchCalls;
import cfwz.skiti.go4lunch.utils.BaseFragment;

public class ListFragment extends BaseFragment implements GooglePlaceSearchCalls.Callbacks, GooglePlaceDetailsCalls.Callbacks{

    private RecyclerView mRecyclerView;
    private List<ResultDetails> mResultDetailsList = new ArrayList<>();



    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        GooglePlaceSearchCalls.fetchNearbyRestaurants(this, "-33.8670522,151.1957362");
        return view;
    }




    /**
     * Init the List of neighbours
     */


    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
        getPlaceDetails(resultSearchList);
    }

    private void getPlaceDetails(List<ResultSearch> resultSearchList) {
        for (int i=0;i<20;i++)
        {
            GooglePlaceDetailsCalls.fetchPlaceDetails(this, resultSearchList.get(i).getPlaceId());
        }
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        mResultDetailsList.add(resultDetails);
        if (mResultDetailsList.size()==20) mRecyclerView.setAdapter(new ListRecyclerViewAdapter(mResultDetailsList));
    }

    @Override
    public void onFailure() {

    }
}