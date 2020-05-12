package cfwz.skiti.go4lunch.models;

/**
 * Created by Skiti on 23/03/2020
 */

public class Restaurant {
    private String placeId;
    private String name;
    private String urlAvatar;
    private String adress;
    private String openingTime;
    private String website;

    private int distance;
    private long rating;
    private int workmatesHere;
    private int phone;

    public Restaurant(){}

    public Restaurant(String placeId,String name,String urlAvatar, String adress, String openingTime, String website, int distance, long rating, int workmatesHere, int phone) {
        this.placeId = placeId;
        this.name = name;
        this.urlAvatar = urlAvatar;
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
    public String getUrlAvatar() { return urlAvatar; }
    public String getAdress() { return adress; }
    public String getOpeningTime() { return openingTime; }
    public String getWebsite() { return website; }
    public int getDistance() { return distance; }
    public long getRating() { return rating; }
    public int getWorkmatesHere() { return workmatesHere; }
    public int getPhone() { return phone; }

    // SETTER
    public void setPlaceId(String placeId) { this.placeId = placeId; }
    public void setName(String name) { this.name = name; }
    public void setUrlAvatar(String urlAvatar) { this.urlAvatar = urlAvatar; }
    public void setAdress(String adress) { this.adress = adress; }
    public void setOpeningTime(String openingTime) { this.openingTime = openingTime; }
    public void setWebsite(String website) { this.website = website; }
    public void setDistance(int distance) { this.distance = distance; }
    public void setRating(long rating) { this.rating = rating; }
    public void setWorkmatesHere(int workmatesHere) { this.workmatesHere = workmatesHere; }
    public void setPhone(int phone) { this.phone = phone; }
}
