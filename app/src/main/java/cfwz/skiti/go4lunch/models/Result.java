package cfwz.skiti.go4lunch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

        @SerializedName("business_status")
        @Expose
        private String businessStatus;
        @SerializedName("formatted_address")
        @Expose
        private String formattedAddress;
        @SerializedName("formatted_phone_number")
        @Expose
        private String formattedPhoneNumber;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("rating")
        @Expose
        private Double rating;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;
        @SerializedName("photos")
        @Expose
        private List<Photo> photos = null;
        @SerializedName("plus_code")
        @Expose
        private PlusCode plusCode;
        @SerializedName("price_level")
        @Expose
        private Integer priceLevel;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("scope")
        @Expose
        private String scope;
        @SerializedName("types")
        @Expose
        private List<String> types = null;
        @SerializedName("user_ratings_total")
        @Expose
        private Integer userRatingsTotal;
        @SerializedName("vicinity")
        @Expose
        private String vicinity;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }

        public PlusCode getPlusCode() {
            return plusCode;
        }

        public void setPlusCode(PlusCode plusCode) {
            this.plusCode = plusCode;
        }

        public Integer getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(Integer priceLevel) {
            this.priceLevel = priceLevel;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public Integer getUserRatingsTotal() {
            return userRatingsTotal;
        }

        public void setUserRatingsTotal(Integer userRatingsTotal) {
            this.userRatingsTotal = userRatingsTotal;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public String getBusinessStatus() {
            return businessStatus;
        }

        public void setBusinessStatus(String businessStatus) {
            this.businessStatus = businessStatus;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public void setFormattedAddress(String formattedAddress) {
            this.formattedAddress = formattedAddress;
        }

        public String getFormattedPhoneNumber() {
            return formattedPhoneNumber;
        }

        public void setFormattedPhoneNumber(String formattedPhoneNumber) {
            this.formattedPhoneNumber = formattedPhoneNumber;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

    }