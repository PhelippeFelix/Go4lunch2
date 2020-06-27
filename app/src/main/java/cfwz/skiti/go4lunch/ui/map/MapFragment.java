package cfwz.skiti.go4lunch.ui.map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.api.RestaurantsHelper;
import cfwz.skiti.go4lunch.model.autocomplete.AutoCompleteResult;
import cfwz.skiti.go4lunch.api.GoogleAutoCompleteCalls;
import cfwz.skiti.go4lunch.ui.restaurant_profile.ProfileActivity;
import cfwz.skiti.go4lunch.ui.BaseFragment;
import cfwz.skiti.go4lunch.ui.MainActivity;
import pub.devrel.easypermissions.EasyPermissions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.googleplaces.ResultDetails;
import cfwz.skiti.go4lunch.model.googleplaces.ResultSearch;
import cfwz.skiti.go4lunch.api.GooglePlaceDetailsCalls;
import cfwz.skiti.go4lunch.api.GooglePlaceSearchCalls;


public class MapFragment extends BaseFragment implements LocationListener, GooglePlaceDetailsCalls.Callbacks, GooglePlaceSearchCalls.Callbacks,GoogleApiClient.OnConnectionFailedListener, GoogleAutoCompleteCalls.Callbacks, GoogleApiClient.ConnectionCallbacks {
    @BindView(R.id.map) MapView mMapView;
    @BindView(R.id.fragment_map_floating_action_btn) FloatingActionButton mFloatingActionButton;

    private static final int PERMS_FINE_COARSE_LOCATION = 100;
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private MapViewModel mViewModel;
    private List<ResultDetails> mResultDetails;
    private int resultSize;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mViewModel.currentUserPosition.observe(getViewLifecycleOwner(), latLng -> GooglePlaceSearch());
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        this.configureMapView();
        this.configureGoogleApiClient();
        this.configureLocationRequest();
        this.configureLocationCallBack();
            return view;
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
                if (query.length() > 2 ){
                    GoogleAutoCompleteSearch(query);
                }else{
                    Toast.makeText(getContext(), getResources().getString(R.string.search_too_short), Toast.LENGTH_LONG).show();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2){
                    GoogleAutoCompleteSearch(query);
                } else if (query.length() < 2){
                    GooglePlaceSearch();
                }
                return false;
            }
        });
    }

    private void GoogleAutoCompleteSearch(String query) {
        GoogleAutoCompleteCalls.fetchAutoCompleteResult(this,query,mViewModel.getCurrentUserPositionFormatted());
    }

    private void GooglePlaceSearch(){
        GooglePlaceSearchCalls.fetchNearbyRestaurants(this,mViewModel.getCurrentUserPositionFormatted());
    }

    private void configureMapView() {
        try {
            MapsInitializer.initialize(getActivity().getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;
            if (checkLocationPermission()) {
                googleMap.setMyLocationEnabled(true);
            }
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).
                    getParent()).findViewById(Integer.parseInt("2"));
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.setOnMarkerClickListener(MapFragment.this::onClickMarker);
        });
    }

    private void updateUI(List<ResultDetails> results){
        googleMap.clear();
        Log.e(TAG, "updateUI: " + results.size());
            if (results.size() > 0){
                for (int i = 0; i < results.size(); i++) {
                    int CurrentObject = i;
                    RestaurantsHelper.getTodayBooking(results.get(CurrentObject).getPlaceId(), getTodayDate()).addOnCompleteListener(restaurantTask -> {
                        if (restaurantTask.isSuccessful()) {
                            Double lat = results.get(CurrentObject).getGeometry().getLocation().getLat();
                            Double lng = results.get(CurrentObject).getGeometry().getLocation().getLng();
                            String title = results.get(CurrentObject).getName();

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(lat, lng));
                            markerOptions.title(title);
                            if (restaurantTask.getResult().isEmpty()) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.lunch_marker_nobody));
                            } else {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.lunch_marker_someone_here));
                            }
                            Marker marker = googleMap.addMarker(markerOptions);
                            marker.setTag(results.get(CurrentObject).getPlaceId());
                        }
                    });
                }
            }else{
                Toast.makeText(getContext(), getResources().getString(R.string.no_restaurant_error_message), Toast.LENGTH_SHORT).show();
            }
        }

    private void handleNewLocation(Location location) {
        Log.e(TAG, "handleNewLocation: " );
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        this.mViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
        this.mViewModel.updateCurrentUserZoom(15);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(this.mViewModel.getCurrentUserPosition()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.mViewModel.getCurrentUserPosition(), mViewModel.getCurrentUserZoom()));
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        if (checkLocationPermission()){
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void configureLocationRequest(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100 * 1000)
                .setFastestInterval(1000);
    }

    private void configureLocationCallBack() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    handleNewLocation(location);
                }
            }
        };
    }

    public boolean checkLocationPermission() {
        return EasyPermissions.hasPermissions(getContext(), perms);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {googleMap.setMyLocationEnabled(true);}
    }

    private boolean onClickMarker(Marker marker){
        if (marker.getTag() != null){
            Log.e(TAG, "onClickMarker: " + marker.getTag() );
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra("PlaceDetailResult", marker.getTag().toString());
            startActivity(intent);
            return true;
        }else{
            Log.e(TAG, "onClickMarker: ERROR NO TAG" );
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
    mResultDetails.add(resultDetails);
    if(mResultDetails.size()==resultSize)updateUI(mResultDetails);
    }

    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
    resultSize=resultSearchList.size();
    SearchToDetails(resultSearchList);
    }

    private void SearchToDetails(List<ResultSearch> resultSearchList) {
        mResultDetails = new ArrayList<>();
        for (int i=0;i<resultSearchList.size();i++){
            GooglePlaceDetailsCalls.fetchPlaceDetails(this,resultSearchList.get(i).getPlaceId());
        }
    }

    @Override
    public void onResponse(@Nullable AutoCompleteResult autoCompleteResult) {
        resultSize=autoCompleteResult.getPredictions().size();
        AutoCompleteToDetails(autoCompleteResult);
    }

    private void AutoCompleteToDetails(AutoCompleteResult autoCompleteResult) {
        mResultDetails = new ArrayList<>();
        for (int i=0; i<autoCompleteResult.getPredictions().size();i++){
            GooglePlaceDetailsCalls.fetchPlaceDetails(this,autoCompleteResult.getPredictions().get(i).getPlaceId());
        }
    }

    @Override
    public void onFailure() {
    }

    private void configureGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .enableAutoManage(getActivity(), this)
                .build();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v)
                                {
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(mViewModel.getCurrentUserPosition()));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mViewModel.getCurrentUserPosition(), mViewModel.getCurrentUserZoom()));
                                }
                            });
                            handleNewLocation(location);
                        } else {
                            if (EasyPermissions.hasPermissions(getContext(), perms)) {
                                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                            }

                        }
                    });
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_perm_access),
                    PERMS_FINE_COARSE_LOCATION, perms);
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect(); }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModel.currentUserPosition.removeObservers(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
}


