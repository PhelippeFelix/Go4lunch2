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

    public Restaurant(){}

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

    // GETTER
    public String getPlaceId() { return placeId; }
    public String getName() { return name; }
    public String getAdress() { return adress; }
    public String getOpeningTime() { return openingTime; }
    public String getWebsite() { return website; }
    public int getDistance() { return distance; }
    public int getRating() { return rating; }
    public int getWorkmatesHere() { return workmatesHere; }
    public int getPhone() { return phone; }

    // SETTER
    public void setPlaceId(String placeId) { this.placeId = placeId; }
    public void setName(String name) { this.name = name; }
    public void setAdress(String adress) { this.adress = adress; }
    public void setOpeningTime(String openingTime) { this.openingTime = openingTime; }
    public void setWebsite(String website) { this.website = website; }
    public void setDistance(int distance) { this.distance = distance; }
    public void setRating(int rating) { this.rating = rating; }
    public void setWorkmatesHere(int workmatesHere) { this.workmatesHere = workmatesHere; }
    public void setPhone(int phone) { this.phone = phone; }
}
