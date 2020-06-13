package cfwz.skiti.go4lunch.ui.restaurant_profile;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.api.RestaurantsHelper;
import cfwz.skiti.go4lunch.api.UserHelper;
import cfwz.skiti.go4lunch.model.GooglePlaces.PlaceDetails;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultDetails;
import cfwz.skiti.go4lunch.model.Workmate;
import cfwz.skiti.go4lunch.stream.GooglePlaceDetailsCalls;
import cfwz.skiti.go4lunch.utils.MainActivity;
import cfwz.skiti.go4lunch.R;

import static cfwz.skiti.go4lunch.ui.list.ListViewHolder.BASE_URL;
import static cfwz.skiti.go4lunch.ui.list.ListViewHolder.MAX_HEIGHT;
import static cfwz.skiti.go4lunch.ui.list.ListViewHolder.MAX_RATING;
import static cfwz.skiti.go4lunch.ui.list.ListViewHolder.MAX_STAR;
import static cfwz.skiti.go4lunch.ui.list.ListViewHolder.MAX_WIDTH;

/**
 * Created by Skiti on 04/03/2020
 */

public class ProfileActivity extends MainActivity implements View.OnClickListener, GooglePlaceDetailsCalls.Callbacks {
    @Nullable @BindView(R.id.restaurant_name) TextView mRestaurantName;
    @Nullable @BindView(R.id.restaurant_address)TextView mRestaurantAddress;
    @Nullable @BindView(R.id.restaurant_recycler_view) RecyclerView mRestaurantRecyclerView;
    @Nullable @BindView(R.id.imageView) ImageView mImageView;
    @Nullable @BindView(R.id.floatingActionButton) FloatingActionButton mFloatingActionButton;
    @Nullable @BindView(R.id.restaurant_item_call) Button mButtonCall;
    @Nullable @BindView(R.id.restaurant_item_like) Button mButtonLike;
    @Nullable @BindView(R.id.restaurant_item_website) Button mButtonWebsite;
    @Nullable @BindView(R.id.item_ratingBar) RatingBar mRatingBar;

    private ResultDetails requestResult;

    private List<Workmate> mWorkmates = new ArrayList<>();
    private ProfileAdapter mProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        ButterKnife.bind(this);


        this.setFloatingActionButtonOnClickListener();
        this.configureButtonClickListener();
        this.configureRecyclerView();
        this.retrieveObject();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void checkIfUserLikeThisRestaurant(){
        RestaurantsHelper.getAllLikeByUserId(getCurrentUser().getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.e("TAG", "checkIfUserLikeThisRestaurant: " + task.getResult().getDocuments());
                if (task.getResult().isEmpty()){ // User don't like any restaurant
                    mButtonLike.setText(getResources().getString(R.string.like_option));
                }else{
                    for (DocumentSnapshot restaurant : task.getResult()){
                        if (restaurant.getId().equals(requestResult.getPlaceId())){
                            mButtonLike.setText(getResources().getString(R.string.unlike_option));
                            break;
                        } else{
                            mButtonLike.setText(getResources().getString(R.string.like_option));
                        }
                    }
                }
            }
        });

    }

    // Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView(){
        this.mProfileAdapter = new ProfileAdapter(this.mWorkmates);
        this.mRestaurantRecyclerView.setAdapter(this.mProfileAdapter);
        this.mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void retrieveObject(){
        String result = getIntent().getStringExtra("PlaceDetailResult");
        Log.e("TAG", "retrieveObject: " + result );
        GooglePlaceDetailsCalls.fetchPlaceDetails(this,result);
    }

    private void setFloatingActionButtonOnClickListener(){
        mFloatingActionButton.setOnClickListener(view -> bookThisRestaurant());
    }

    private void configureButtonClickListener(){
        mButtonCall.setOnClickListener(this);
        mButtonLike.setOnClickListener(this);
        mButtonWebsite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restaurant_item_call:
                if (requestResult.getFormattedPhoneNumber() != null){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+requestResult.getFormattedPhoneNumber()));
                    startActivity(intent);
                }else{
                    Toast.makeText(this, getResources().getString(R.string.restaurant_detail_no_phone), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.restaurant_item_like:
                if (mButtonLike.getText().equals(getResources().getString(R.string.like_option))){
                    this.likeThisRestaurant();
                }else{
                    this.dislikeThisRestaurant();
                }
                break;

            case R.id.restaurant_item_website:
                if (requestResult.getWebsite() != null){
                    Intent intent = new Intent(this,WebViewActivity.class);
                    intent.putExtra("Website", requestResult.getWebsite());
                    startActivity(intent);
                }else{
                    Toast.makeText(this, getResources().getString(R.string.restaurant_detail_no_website), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(ResultDetails results){
            if (getCurrentUser() != null){
                this.checkIfUserAlreadyBookedRestaurant(getCurrentUser().getUid(),requestResult.getPlaceId(),requestResult.getName(),false);
                this.checkIfUserLikeThisRestaurant();
            }else{
                mButtonLike.setText(R.string.like_option);
                this.displayFAB((R.drawable.ic_check_circle_black_24dp),getResources().getColor(R.color.colorGreen));
                Toast.makeText(this, getResources().getString(R.string.restaurant_error_retrieving_info), Toast.LENGTH_SHORT).show();
            }
            Picasso.with(this).load(BASE_URL+"?maxwidth="+MAX_WIDTH+"&maxheight="+MAX_HEIGHT+"&photoreference="+results.getPhotos().get(0).getPhotoReference()+"&key="+ "AIzaSyBm5KR0R5LIAKLlQZoiodV6rbQ61iClmL4").into(mImageView);
            mRestaurantName.setText(results.getName());
            mRestaurantAddress.setText(results.getVicinity());
            this.displayRating(results);
            this.updateUIWithRecyclerView(results.getPlaceId());
        }

    private void updateUIWithRecyclerView(String placeId){
        mWorkmates.clear();
        RestaurantsHelper.getTodayBooking(placeId, getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()){
                if (restaurantTask.getResult().isEmpty()){
                    mProfileAdapter.notifyDataSetChanged();
                }else{
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()){
                        Log.e("TAG", "PROFILE_ACTIVITY | Restaurant : " + restaurant.getData() );
                        UserHelper.getWorkmate(restaurant.getData().get("userId").toString()).addOnCompleteListener(workmateTask -> {
                            if (workmateTask.isSuccessful()){
                                Log.e("TAG", "PROFILE_ACTIVITY | User : " + workmateTask.getResult() );
                                String uid = workmateTask.getResult().getData().get("uid").toString();
                                String name = workmateTask.getResult().getData().get("name").toString();
                                String urlPicture = workmateTask.getResult().getData().get("urlPicture").toString();
                                Workmate workmateToAdd = new Workmate(uid,urlPicture,name);
                                mWorkmates.add(workmateToAdd);
                            }
                            mProfileAdapter.notifyDataSetChanged();
                        });
                    }
                }
            }
        });
    }

    // --------------------
    // REST REQUEST
    // --------------------

    private void likeThisRestaurant(){
        if (requestResult != null && getCurrentUser() != null){
            RestaurantsHelper.createLike(requestResult.getPlaceId(),getCurrentUser().getUid()).addOnCompleteListener(likeTask -> {
                if (likeTask.isSuccessful()) {
                    Toast.makeText(this, getResources().getString(R.string.restaurant_like_ok), Toast.LENGTH_SHORT).show();
                    mButtonLike.setText(getResources().getString(R.string.unlike_option));
                }
            });
        }else{
            Toast.makeText(this, getResources().getString(R.string.restaurant_like_ko), Toast.LENGTH_SHORT).show();
        }
    }

    private void dislikeThisRestaurant(){
        if (requestResult != null && getCurrentUser() != null){
            RestaurantsHelper.deleteLike(requestResult.getPlaceId(), getCurrentUser().getUid());
            mButtonLike.setText(getResources().getString(R.string.like_option));
            Toast.makeText(this, getResources().getString(R.string.restaurant_dislike_ok), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getResources().getString(R.string.restaurant_like_ko), Toast.LENGTH_SHORT).show();
        }
    }

    private void bookThisRestaurant(){
        if (this.getCurrentUser() != null){
            String userId = getCurrentUser().getUid();
            String restaurantId = requestResult.getPlaceId();
            String restaurantName = requestResult.getName();
            this.checkIfUserAlreadyBookedRestaurant(userId,restaurantId, restaurantName, true);
        }else{
            Log.e("TAG", "USER : DISCONNECTED" );
        }
    }

    // ---------------------------------
    // PROCESS TO BOOK A RESTAURANT
    // ---------------------------------

    private void checkIfUserAlreadyBookedRestaurant(String userId, String restaurantId, String restaurantName, Boolean tryingToBook){
        RestaurantsHelper.getBooking(userId, getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()){
                if (restaurantTask.getResult().size() == 1){ // User already booked a restaurant today

                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        if (restaurant.getData().get("restaurantName").equals(restaurantName)){ // If booked restaurant is the same as restaurant we are trying to book
                            this.displayFAB((R.drawable.ic_clear_black_24dp),getResources().getColor(R.color.colorError));
                            if (tryingToBook){
                                this.manageBooking(userId, restaurantId, restaurantName,restaurant.getId(),false,false,true);
                                Toast.makeText(this, getResources().getString(R.string.restaurant_cancel_booking), Toast.LENGTH_SHORT).show();
                            }

                        }else{ // If user is trying to book an other restaurant for today
                            this.displayFAB((R.drawable.ic_check_circle_black_24dp),getResources().getColor(R.color.colorGreen));
                            if (tryingToBook){
                                this.manageBooking(userId, restaurantId, restaurantName,restaurant.getId(),false,true,false);
                                Toast.makeText(this, getResources().getString(R.string.restaurant_change_booking), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }else{ // No restaurant booked for this user today
                    this.displayFAB((R.drawable.ic_check_circle_black_24dp),getResources().getColor(R.color.colorGreen));
                    if (tryingToBook){
                        this.manageBooking(userId, restaurantId, restaurantName,null,true,false,false);
                        Toast.makeText(this, getResources().getString(R.string.restaurant_new_booking), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void manageBooking(String userId, String restaurantId,String restaurantName,@Nullable String bookingId, boolean toCreate, boolean toUpdate, boolean toDelete){
        if(toUpdate){
            RestaurantsHelper.deleteBooking(bookingId);
            RestaurantsHelper.createBooking(this.getTodayDate(),userId,restaurantId, restaurantName).addOnFailureListener(this.onFailureListener());
            this.displayFAB((R.drawable.ic_clear_black_24dp),getResources().getColor(R.color.colorError));
        }else if(toCreate){
            RestaurantsHelper.createBooking(this.getTodayDate(),userId,restaurantId, restaurantName).addOnFailureListener(this.onFailureListener());
            this.displayFAB((R.drawable.ic_clear_black_24dp),getResources().getColor(R.color.colorError));
        }else if(toDelete){
            RestaurantsHelper.deleteBooking(bookingId);
            this.displayFAB((R.drawable.ic_check_circle_black_24dp),getResources().getColor(R.color.colorGreen));
        }

        updateUIWithRecyclerView(requestResult.getPlaceId());
    }

    private void displayFAB(int icon, int color){
        Drawable mDrawable = ContextCompat.getDrawable(getBaseContext(), icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        mFloatingActionButton.setImageDrawable(mDrawable);
    }

    private void displayRating(ResultDetails results){
        if (results.getRating() != null){
            double googleRating = results.getRating();
            double rating = googleRating / MAX_RATING * MAX_STAR;
            this.mRatingBar.setRating((float)rating);
            this.mRatingBar.setVisibility(View.VISIBLE);
        }else{
            this.mRatingBar.setVisibility(View.GONE);
        }
    }

    protected String getTodayDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(c.getTime());
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        this.requestResult = resultDetails;
        updateUI(resultDetails);
    }

    @Override
    public void onFailure() {

    }
}

