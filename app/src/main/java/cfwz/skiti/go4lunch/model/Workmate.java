package cfwz.skiti.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Skiti on 23/03/2020
 */

public class Workmate {
    @Nullable String urlPicture;
    private String name;
    private String restaurant;
    private ArrayList<String>favorite = new ArrayList<>();

    public Workmate() {}

    public Workmate(@Nullable String urlPicture, String name, String restaurant, ArrayList<String> favorite) {
        this.urlPicture = urlPicture;
        this.name = name;
        this.restaurant = restaurant;
        this.favorite = favorite;
    }


    //GETTER
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public String getName() { return name; }
    public String getRestaurant() { return restaurant; }
    public ArrayList<String> getFavorite() { return favorite; }


    //SETTER
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setName(String name) { this.name = name; }
    public void setRestaurant(String restaurant) { this.restaurant = restaurant; }
    public void setFavorite(ArrayList<String> favorite) { this.favorite = favorite; }
}
