package cfwz.skiti.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Skiti on 23/03/2020
 */

public class Workmate {
    private String uid;
    private @Nullable String urlPicture;
    private String name;

    public Workmate() {}

    public Workmate(String uid,@Nullable String urlPicture, String name) {
        this.uid = uid;
        this.urlPicture = urlPicture;
        this.name = name; }


    //GETTER
    public String getUid() { return uid; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public String getName() { return name; }


    //SETTER
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setName(String name) { this.name = name; }
}
