
package cfwz.skiti.go4lunch.model.GooglePlaces;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PlaceDetails {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("result")
    private ResultDetails mResultDetails;
    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public ResultDetails getResultDetails() {
        return mResultDetails;
    }

    public void setResultDetails(ResultDetails resultDetails) {
        mResultDetails = resultDetails;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
