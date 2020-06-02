package cfwz.skiti.go4lunch.ui.workmates;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.api.RestaurantsHelper;
import cfwz.skiti.go4lunch.model.Workmate;

/**
 * Created by Skiti on 02/06/2020
 */

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_list_avatar) public ImageView mWorkmateAvatar;
        @BindView(R.id.item_list_name) public TextView mWorkmakeName;

        public WorkmatesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view); }


    public void updateWithData(Workmate results){
        RequestManager glide = Glide.with(itemView);
        if (!(results.getUrlPicture() == null)){
            glide.load(results.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mWorkmateAvatar);
        }else{
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.circleCropTransform()).into(mWorkmateAvatar);
        }
        RestaurantsHelper.getBooking(results.getUid(), getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()){
                if (restaurantTask.getResult().size() == 1){ // User already booked a restaurant today
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        this.mWorkmakeName.setText(itemView.getResources().getString(R.string.mates_is_eating_at, results.getName(), restaurant.getData().get("restaurantName")));
                        this.changeTextColor(R.color.colorBlack);
                    }
                }else{ // No restaurant booked for this user today
                    this.mWorkmakeName.setText(itemView.getResources().getString(R.string.mates_hasnt_decided, results.getName()));
                    this.changeTextColor(R.color.colorGray);
                }
            }
        });
    }


        private void changeTextColor(int color){
            int mColor = itemView.getContext().getResources().getColor(color);
            this.mWorkmakeName.setTextColor(mColor);
        }


        private String getTodayDate(){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(c.getTime());
        }
    }