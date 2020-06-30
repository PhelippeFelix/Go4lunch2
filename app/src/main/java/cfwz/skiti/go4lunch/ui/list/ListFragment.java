package cfwz.skiti.go4lunch.ui.list;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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

import com.google.android.gms.location.LocationListener;
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


public class ListFragment extends BaseFragment implements LocationListener {
    private RecyclerView mRecyclerView;
    private ListRecyclerViewAdapter mViewAdapter;
    private MainActivity mainActivity;


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Context context = view.getContext();
        mainActivity.mLiveData.observe(getViewLifecycleOwner(),resultDetails -> configureRecyclerView() );
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        setHasOptionsMenu(true);
        configureOnClickRecyclerView();
        return view;
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
                    mainActivity.GoogleAutoCompleteSearch(query);
                    mViewAdapter.notifyDataSetChanged();
                    searchView.clearFocus();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.search_too_short), Toast.LENGTH_LONG).show();
                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2) {
                    mainActivity.GoogleAutoCompleteSearch(query);
                    mViewAdapter.notifyDataSetChanged();
                } else if (query.length() == 0 ) {
                    mainActivity.resetList();
                    mViewAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        mainActivity.mViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
    }

    private void configureRecyclerView() {
        this.mViewAdapter = new ListRecyclerViewAdapter(mainActivity.mLiveData.getValue(), mainActivity.mViewModel.getCurrentUserPositionFormatted());
        this.mRecyclerView.setAdapter(this.mViewAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}