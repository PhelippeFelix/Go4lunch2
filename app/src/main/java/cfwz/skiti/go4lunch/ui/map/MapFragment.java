package cfwz.skiti.go4lunch.ui.map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import cfwz.skiti.go4lunch.api.RestaurantsHelper;
import cfwz.skiti.go4lunch.model.GooglePlaces.SearchPlace;
import cfwz.skiti.go4lunch.stream.GoogleApi;
import cfwz.skiti.go4lunch.ui.restaurant_profile.ProfileActivity;
import cfwz.skiti.go4lunch.utils.BaseFragment;
import pub.devrel.easypermissions.EasyPermissions;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultDetails;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultSearch;
import cfwz.skiti.go4lunch.stream.GooglePlaceDetailsCalls;
import cfwz.skiti.go4lunch.stream.GooglePlaceSearchCalls;

public class MapFragment extends BaseFragment implements LocationListener, GooglePlaceDetailsCalls.Callbacks, GooglePlaceSearchCalls.Callbacks {

    @BindView(R.id.map) MapView mMapView;
    private static final int PERMS_FINE_COARSE_LOCATION = 100;
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final String SEARCH_TYPE = "restaurant";

    private GoogleMap googleMap;
    private GoogleApi mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private MapViewModel mViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
            super.onCreate(savedInstanceState);

            return view;
        }

    private void configureMapView() {
        try {
            MapsInitializer.initialize(getActivity().getBaseContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (checkLocationPermission()) {
                    //Request location updates:
                    googleMap.setMyLocationEnabled(true);
                }
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).
                        getParent()).findViewById(Integer.parseInt("2"));

                // and next place it, for example, on bottom right (as Google Maps app)
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                googleMap.setOnMarkerClickListener(MapFragment.this::onClickMarker);
            }
        });
    }
    private void updateUI(List<ResultSearch> results){
        googleMap.clear();
        if(results instanceof ResultSearch){
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
                            if (restaurantTask.getResult().isEmpty()) { // If there is no booking for today
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.lunch_marker_nobody));
                            } else { // If there is booking for today
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
    }

    private void handleNewLocation(Location location) {
        Log.e(TAG, "handleNewLocation: " );
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        this.mViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));

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
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100 * 1000)        // 100 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
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
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
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

    }

    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
    updateUI(resultSearchList);
    }

    @Override
    public void onFailure() {

    }
}


