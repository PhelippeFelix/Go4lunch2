
package cfwz.skiti.go4lunch.model.googleplaces;


import com.google.gson.annotations.SerializedName;


public class Geometry {
    @SerializedName("location")
    private Location mLocation;
    @SerializedName("viewport")
    private Viewport mViewport;

    public Location getLocation() {
        return mLocation;
    }
    public void setLocation(Location location) {
        mLocation = location;
    }
    public Viewport getViewport() {
        return mViewport;
    }
    public void setViewport(Viewport viewport) {
        mViewport = viewport;
    }
}
