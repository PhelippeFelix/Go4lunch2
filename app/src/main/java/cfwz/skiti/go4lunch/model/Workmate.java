package cfwz.skiti.go4lunch.model;

import java.util.ArrayList;

/**
 * Created by Skiti on 23/03/2020
 */

public class Workmate {
    private String name;
    private String restaurant;
    private ArrayList<String>favorite = new ArrayList<>();

    public Workmate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<String> favorite) {
        this.favorite = favorite;
    }

    public void Favorite(Workmate workmate,Restaurant restaurant){
        if (workmate.getFavorite().contains(restaurant.getPlaceId())){
            workmate.getFavorite().remove((restaurant.getPlaceId()));
        }else{
            workmate.getFavorite().add(restaurant.getPlaceId());
        }
    }
}
