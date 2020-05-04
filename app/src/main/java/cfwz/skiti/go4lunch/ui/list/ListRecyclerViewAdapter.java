package cfwz.skiti.go4lunch.ui.workmates;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.models.Restaurant;
import cfwz.skiti.go4lunch.models.Workmate;

import static androidx.core.content.ContextCompat.startActivity;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder > {

    private final List<Restaurant> mRestaurants;
    private String restaurant;

    public ListRecyclerViewAdapter(List<Restaurant> items) { mRestaurants = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_object_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        holder.mNameRestaurant.setText(restaurant.getName());
        Glide.with(holder.mAvatarRestaurant.getContext())
                .load(restaurant.get())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mAvatarRestaurant);
        
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_restaurant) public TextView mNameRestaurant;
        @BindView(R.id.adress_restaurant) public TextView mAdressRestaurant;
        @BindView(R.id.Open_hour) public TextView mOpenHour;
        @BindView(R.id.distance_restaurant) public TextView mDistance;
        @BindView(R.id.star_restaurant) public ImageView mStar1;
        @BindView(R.id.star_restaurant2) public ImageView mStar2;
        @BindView(R.id.star_restaurant3) public ImageView mStar3;
        @BindView(R.id.workmate_on_restaurant) public TextView mWorkmateOnIt;
        @BindView(R.id.item_avatar_restaurant) public ImageView mAvatarRestaurant;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}