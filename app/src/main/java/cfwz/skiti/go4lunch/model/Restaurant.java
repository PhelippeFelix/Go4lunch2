package cfwz.skiti.go4lunch.model;

/**
 * Created by Skiti on 23/03/2020
 */

public class Restaurant {
    private String placeId;
    private String name;
    private String adress;
    private String openingTime;
    private String website;

    private int distance;
    private int rating;
    private int workmatesHere;
    private int phone;

    public Restaurant(String placeId,String name, String adress, String openingTime, String website, int distance, int rating, int workmatesHere, int phone) {
        this.placeId = placeId;
        this.name = name;
        this.adress = adress;
        this.openingTime = openingTime;
        this.website = website;
        this.distance = distance;
        this.rating = rating;
        this.workmatesHere = workmatesHere;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int note) {
        this.rating = note;
    }

    public int getWorkmatesHere() {
        return workmatesHere;
    }

    public void setWorkmatesHere(int workmatesHere) {
        this.workmatesHere = workmatesHere;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
