package cfwz.skiti.go4lunch.ui.list;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.autocomplete.AutoCompleteResult;
import cfwz.skiti.go4lunch.model.googleplaces.ResultDetails;
import cfwz.skiti.go4lunch.model.googleplaces.ResultSearch;
import cfwz.skiti.go4lunch.api.GoogleAutoCompleteCalls;
import cfwz.skiti.go4lunch.api.GooglePlaceDetailsCalls;
import cfwz.skiti.go4lunch.api.GooglePlaceSearchCalls;
import cfwz.skiti.go4lunch.ui.map.MapViewModel;
import cfwz.skiti.go4lunch.ui.restaurant_profile.ProfileActivity;
import cfwz.skiti.go4lunch.ui.BaseFragment;
import cfwz.skiti.go4lunch.utils.ItemClickSupport;
import cfwz.skiti.go4lunch.ui.MainActivity;
import pub.devrel.easypermissions.EasyPermissions;


public class ListFragment extends BaseFragment implements GooglePlaceSearchCalls.Callbacks, GooglePlaceDetailsCalls.Callbacks, GoogleAutoCompleteCalls.Callbacks, LocationListener {
    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private RecyclerView mRecyclerView;
    private List<ResultDetails> mResultDetailsList;
    private ListRecyclerViewAdapter mViewAdapter;
    private MapViewModel mViewModel;
    private int resultSize;


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Context context = view.getContext();
        checkLocationPermission();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            if (location != null) {
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();
                mViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
            }
        });
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mViewModel.currentUserPosition.observe(getViewLifecycleOwner(), latLng -> GooglePlaceSearch());

        setHasOptionsMenu(true);
        configureOnClickRecyclerView();
        return view;
    }

    private void GooglePlaceSearch() {
        GooglePlaceSearchCalls.fetchNearbyRestaurants(this, mViewModel.getCurrentUserPositionFormatted());
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_restaurant_object_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    ResultDetails result = mViewAdapter.getRestaurantDetails(position);
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("PlaceDetailResult", result.getPlaceId());
                    startActivity(intent);
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.activity_main_appbar, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.menu_activity_main_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setQueryHint(getResources().getString(R.string.toolbar_search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(((MainActivity) getContext()).getComponentName()));
        searchView.setIconifiedByDefault(false);// Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    GoogleAutoCompleteSearch(query);
                    searchView.clearFocus();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.search_too_short), Toast.LENGTH_LONG).show();
                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2) {
                    System.out.println("tictic");
                    GoogleAutoCompleteSearch(query);
                } else if (query.length() == 0) {
                    System.out.println("tactac");
                    GooglePlaceSearch();
                }
                return false;
            }
        });
    }

    private void GoogleAutoCompleteSearch(String query) {
        GoogleAutoCompleteCalls.fetchAutoCompleteResult(this, query, mViewModel.getCurrentUserPositionFormatted());
    }

    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
        mResultDetailsList = new ArrayList<>();
        resultSize = resultSearchList.size();
        SearchToDetails(resultSearchList);
    }

    private void SearchToDetails(List<ResultSearch> resultSearchList) {
        mResultDetailsList.clear();
        for (int i = 0; i < resultSearchList.size(); i++) {
            GooglePlaceDetailsCalls.fetchPlaceDetails(this, resultSearchList.get(i).getPlaceId());
        }
    }

    private void AutoCompleteToDetails(AutoCompleteResult autoCompleteResult) {
        System.out.println("tac");
        mResultDetailsList.clear();
        for (int i = 0; i < autoCompleteResult.getPredictions().size(); i++) {
            GooglePlaceDetailsCalls.fetchPlaceDetails(this, autoCompleteResult.getPredictions().get(i).getPlaceId());
        }
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        if (resultDetails.getTypes().contains("restaurant")){
            System.out.println(resultSize);
            mResultDetailsList.add(resultDetails);}
        else {
            resultSize--;
        }
        if (mResultDetailsList.size() == resultSize) {
            System.out.println(resultSize+"SDFSDF");
            configureRecyclerView();
        }
    }

    @Override
    public void onResponse(@Nullable AutoCompleteResult autoCompleteResult) {
        resultSize = autoCompleteResult.getPredictions().size();
        AutoCompleteToDetails(autoCompleteResult);
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
        return EasyPermissions.hasPermissions(getContext(), perms);
    }

    private void configureRecyclerView() {
        this.mViewAdapter = new ListRecyclerViewAdapter(this.mResultDetailsList, mViewModel.getCurrentUserPositionFormatted());
        this.mRecyclerView.setAdapter(this.mViewAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}