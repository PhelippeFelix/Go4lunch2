package cfwz.skiti.go4lunch.ui.list;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.api.UserHelper;
import cfwz.skiti.go4lunch.models.Restaurant;
import cfwz.skiti.go4lunch.models.Workmate;
import cfwz.skiti.go4lunch.stream.GoogleApi;
import cfwz.skiti.go4lunch.ui.list.ListRecyclerViewAdapter;

import static android.content.Context.LOCATION_SERVICE;

public class ListFragment extends Fragment {

    private List<Restaurant> mRestaurants = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GoogleApi mGoogleApi = new GoogleApi();



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
        initList();
        return view;
    }


    /**
     * Init the List of neighbours
     */
    private void initList() {
      mRestaurants = mGoogleApi.getRestaurantList("-33.8670522,151.1957362");
      mRecyclerView.setAdapter(new ListRecyclerViewAdapter(mRestaurants));
    }
}