package cfwz.skiti.go4lunch.ui.list;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultDetails;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultSearch;
import cfwz.skiti.go4lunch.stream.GooglePlaceDetailsCalls;
import cfwz.skiti.go4lunch.stream.GooglePlaceSearchCalls;
import cfwz.skiti.go4lunch.ui.map.MapViewModel;
import cfwz.skiti.go4lunch.ui.restaurant_profile.ProfileActivity;
import cfwz.skiti.go4lunch.utils.BaseFragment;
import cfwz.skiti.go4lunch.utils.ItemClickSupport;
import pub.devrel.easypermissions.EasyPermissions;

public class ListFragment extends BaseFragment implements GooglePlaceSearchCalls.Callbacks, GooglePlaceDetailsCalls.Callbacks, LocationListener {

    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private RecyclerView mRecyclerView;
    private List<ResultDetails> mResultDetailsList = new ArrayList<>();
    private List<ResultSearch> mSearchList = new ArrayList<>();
    private ListRecyclerViewAdapter mViewAdapter;
    private MapViewModel mViewModel;
    private String mLocation;



    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Context context = view.getContext();
        mLocation = mViewModel.getCurrentUserPositionFormatted();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        updateList(mLocation);
        return view;
    }

    private void updateList(String location) {
        GooglePlaceSearchCalls.fetchNearbyRestaurants(this, location);
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_restaurant_object_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    ResultDetails result = mViewAdapter.getRestaurantDetails(position);
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("PlaceDetailResult", result.getPlaceId());
                    startActivity(intent);
                });
    }

    /**
     * Init the List of neighbours
     */


    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
        this.mSearchList = resultSearchList;
        getPlaceDetails(resultSearchList);
    }

    private void getPlaceDetails(List<ResultSearch> resultSearchList) {
        for (int i=0;i<resultSearchList.size();i++)
        {
            GooglePlaceDetailsCalls.fetchPlaceDetails(this, resultSearchList.get(i).getPlaceId());
        }
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        mResultDetailsList.add(resultDetails);
        if (mResultDetailsList.size()==mSearchList.size()) mRecyclerView.setAdapter(new ListRecyclerViewAdapter(mResultDetailsList,mViewModel.getCurrentUserPositionFormatted()));
    }

    @Override
    public void onFailure() {
        Toast.makeText(getContext(), getResources().getString(R.string.no_restaurant_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        this.mViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));

    }

    public boolean checkLocationPermission() {
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        System.out.println("EEEEEEEEEEEEEEEEEE");
    }

    @Override
    public void onProviderDisabled(String s) {
        System.out.println("DDDDDDDDDDDDDDDDD");
    }
}