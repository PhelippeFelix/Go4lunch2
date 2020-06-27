package cfwz.skiti.go4lunch.ui.workmates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.api.RestaurantsHelper;
import cfwz.skiti.go4lunch.api.UserHelper;
import cfwz.skiti.go4lunch.model.Workmate;
import cfwz.skiti.go4lunch.ui.restaurant_profile.ProfileActivity;
import cfwz.skiti.go4lunch.ui.BaseFragment;
import cfwz.skiti.go4lunch.utils.ItemClickSupport;

import static com.firebase.ui.auth.ui.email.EmailLinkFragment.TAG;

public class WorkmatesFragment extends BaseFragment {
    private List<Workmate> mWorkmates = new ArrayList<>();
    private WorkmatesRecyclerViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;



    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);
        Context context = view.getContext();
        initList();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        configureOnClickRecyclerView();
        this.mViewAdapter = new WorkmatesRecyclerViewAdapter(this.mWorkmates);
        this.mRecyclerView.setAdapter(this.mViewAdapter);
        return view;
    }


    private void initList() {
        UserHelper.getWorkmatesCollection()
                .get()
                .addOnCompleteListener(task -> {
                    mWorkmates.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mWorkmates.add(document.toObject(Workmate.class));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    Collections.sort(mWorkmates, new Comparator<Workmate>(){
                        public int compare(Workmate obj1, Workmate obj2) {
                            return obj1.getUid().compareToIgnoreCase(obj2.getUid());
                        }
                    });
                    mRecyclerView.setAdapter(new WorkmatesRecyclerViewAdapter(mWorkmates));
                });
    }


    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_workmates_object_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Workmate result = mViewAdapter.getWorkmates(position);
                    ProfileRestaurantByWorkmate(result);
                });
    }


    private void ProfileRestaurantByWorkmate(Workmate workmate){
        RestaurantsHelper.getBooking(workmate.getUid(),getTodayDate()).addOnCompleteListener(bookingTask -> {
            if (bookingTask.isSuccessful()){
                if (!(Objects.requireNonNull(bookingTask.getResult()).isEmpty())){
                    for (QueryDocumentSnapshot booking : bookingTask.getResult()){
                        showBookedRestaurantByUser(Objects.requireNonNull(booking.getData().get("restaurantId")).toString());
                    }
                }else{
                    Toast.makeText(getContext(), getResources().getString(R.string.mates_hasnt_decided,workmate.getName()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showBookedRestaurantByUser(String placeId){
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("PlaceDetailResult", placeId);
        startActivity(intent);
    }
}