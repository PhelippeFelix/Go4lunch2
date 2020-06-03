package cfwz.skiti.go4lunch.ui.list;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.api.RestaurantsHelper;
import cfwz.skiti.go4lunch.model.GooglePlaces.Location;
import cfwz.skiti.go4lunch.model.GooglePlaces.ResultDetails;

/**
 * Created by Skiti on 18/05/2020
 */

public class ListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name_restaurant) public TextView mNameRestaurant;
        @BindView(R.id.adress_restaurant) public TextView mAdressRestaurant;
        @BindView(R.id.Open_hour) public TextView mOpenHour;
        @BindView(R.id.distance_restaurant) public TextView mDistance;
        @BindView(R.id.star_restaurant) public RatingBar mStar;
        @BindView(R.id.workmate_on_restaurant) public TextView mWorkmateOnIt;
        @BindView(R.id.workmate_on_restaurant_image) public ImageView mWorkmateImage;
        @BindView(R.id.item_avatar_restaurant) public ImageView mAvatarRestaurant;

    private static final String OPEN = "OPEN";
    private static final String CLOSED = "CLOSED";
    private static final String CLOSING_SOON = "CLOSING_SOON";
    private static final String OPENING_HOURS_NOT_KNOW = "OPENING_HOURS_NOT_KNOW";

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 75;
    public static final int MAX_HEIGHT = 75;
    public static final int MAX_HEIGHT_LARGE = 250;

    public static final double MAX_RATING = 5;
    public static final double MAX_STAR = 3;

    private static float[] distanceResults = new float[3];


    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(ResultDetails resultDetails, String location) {
        RequestManager glide = Glide.with(itemView);

        // Display Name
        this.mNameRestaurant.setText(resultDetails.getName());

        // Display Distance
        getDistance(location,resultDetails.getGeometry().getLocation());
        String distance = Integer.toString(Math.round(distanceResults[0]));
        this.mDistance.setText(itemView.getResources().getString(R.string.list_unit_distance, distance));

        // Display Address
        this.mAdressRestaurant.setText(getAddress(resultDetails));

        // Display Rating
        displayRating(resultDetails);

        // Display Opening Hours
        if (resultDetails.getOpeningHours() != null){
            if (resultDetails.getOpeningHours().getOpenNow().toString().equals("false")){
                displayOpeningHour(CLOSED,null);
            }else{
                getOpeningHoursInfo(resultDetails);
            }
        }else{
            displayOpeningHour(OPENING_HOURS_NOT_KNOW,null);
        }

        // Display Mates number & Icon
        RestaurantsHelper.getTodayBooking(resultDetails.getPlaceId(),getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()){
                if (restaurantTask.getResult().size() > 0) {
                    for (QueryDocumentSnapshot document : restaurantTask.getResult()) {
                        Log.e("TAG", document.getId() + " => " + document.getData());
                    }
                    this.mWorkmateOnIt.setText(itemView.getResources().getString(R.string.restaurant_mates_number,restaurantTask.getResult().size()));
                    this.mWorkmateImage.setImageResource(R.drawable.ic_perm_identity_black_24dp);
                    this.mWorkmateImage.setVisibility(View.VISIBLE);
                }else{
                    this.mWorkmateOnIt.setText("");
                    this.mWorkmateImage.setVisibility(View.GONE);
                }
            }
        });

        // Display Photos
        if (!(resultDetails.getPhotos() == null)){
            if (!(resultDetails.getPhotos().isEmpty())){
                glide.load(BASE_URL+"?maxwidth="+MAX_WIDTH+"&maxheight="+MAX_HEIGHT+"&photoreference="+resultDetails.getPhotos().get(0).getPhotoReference()+"&key="+ "AIzaSyBm5KR0R5LIAKLlQZoiodV6rbQ61iClmL4").into(mAvatarRestaurant);
            }
        }else{
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.centerCropTransform()).into(mAvatarRestaurant);
        }
    }


    private void displayRating(ResultDetails resultDetails){
        if (resultDetails.getRating() != null){
            double googleRating = resultDetails.getRating();
            double rating = googleRating / MAX_RATING * MAX_STAR;
            this.mStar.setRating((float)rating);
            this.mStar.setVisibility(View.VISIBLE);
        }else{
            this.mStar.setVisibility(View.GONE);
        }
    }

    private String getAddress(ResultDetails resultDetails){
        String addressNumber;
        String addressStreet;

        switch (resultDetails.getAddressComponents().get(0).getTypes().get(0)) {
            case "street_number":
                addressNumber = resultDetails.getAddressComponents().get(0).getLongName();
                if (resultDetails.getAddressComponents().get(1).getTypes().get(0).equals("route")) {
                    addressStreet = resultDetails.getAddressComponents().get(1).getLongName();
                    return addressNumber + " " + addressStreet;
                } else {
                    return resultDetails.getVicinity();
                }
            case "route":
                addressStreet = resultDetails.getAddressComponents().get(0).getLongName();
                return addressStreet;
            default:
                return resultDetails.getVicinity();
        }
    }

    private void getDistance(String startLocation, Location endLocation){
        String[] separatedStart = startLocation.split(",");
        double startLatitude = Double.parseDouble(separatedStart[0]);
        double startLongitude = Double.parseDouble(separatedStart[1]);
        double endLatitude = endLocation.getLat();
        double endLongitude = endLocation.getLng();
        android.location.Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude,distanceResults);
    }

    private void getOpeningHoursInfo(ResultDetails resultDetails){
        int daysArray[] = {0,1,2,3,4,5,6};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minOfDay = calendar.get(Calendar.MINUTE);
        if (minOfDay < 10){minOfDay = '0'+minOfDay;}
        String currentHourString = Integer.toString(hourOfDay)+Integer.toString(minOfDay);
        int currentHour = Integer.parseInt(currentHourString);

        for (int i=0;i < resultDetails.getOpeningHours().getPeriods().size();i++){
            if (resultDetails.getOpeningHours().getPeriods().get(i).getOpen().getDay() == daysArray[day]){
                String closeHour = resultDetails.getOpeningHours().getPeriods().get(i).getClose().getTime();
                if (currentHour < Integer.parseInt(closeHour) || daysArray[day] < resultDetails.getOpeningHours().getPeriods().get(i).getClose().getDay()){
                    int timeDifference = Integer.parseInt(closeHour) - currentHour;
                    //Log.e("TAG", "RestaurantName : " + results.getName() + " | CurrentHour : " + currentHour + " | CloseHour : " + Integer.parseInt(closeHour) + " | TimeDifference : " + timeDifference);
                    if (timeDifference <= 30 && daysArray[day] == resultDetails.getOpeningHours().getPeriods().get(i).getClose().getDay()){
                        displayOpeningHour(CLOSING_SOON, closeHour);
                    }else{
                        displayOpeningHour(OPEN,resultDetails.getOpeningHours().getPeriods().get(i).getClose().getTime());
                    }
                    break;
                }
            }
        }
    }

    private void displayOpeningHour(String type, String hour){
        switch (type){
            case OPEN:
                this.mOpenHour.setText(itemView.getResources().getString(R.string.restaurant_open_until,formatTime(itemView.getContext(),hour)));
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreen));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case CLOSED:
                this.mOpenHour.setText(R.string.restaurant_closed);
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorError));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case CLOSING_SOON:
                this.mOpenHour.setText(itemView.getResources().getString(R.string.restaurant_closing_soon,formatTime(itemView.getContext(),hour)));
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorCloseSoon));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case OPENING_HOURS_NOT_KNOW:
                this.mOpenHour.setText(R.string.restaurant_opening_not_know);
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorCloseSoon));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
        }
    }

    private String formatTime(Context context, String date) {
        date = date.substring(0,2) + ":" +date.substring(2);
        try {
            Date date1 = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(date);
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return dateFormat.format(date1);
            }else{
                SimpleDateFormat dateFormat = new SimpleDateFormat("h.mm a", Locale.getDefault());
                return dateFormat.format(date1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTodayDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(c.getTime());
    }
}
