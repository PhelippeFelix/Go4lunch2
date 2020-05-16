package cfwz.skiti.go4lunch.ui.restaurant_profile;

import android.os.Bundle;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.utils.MainActivity;
import cfwz.skiti.go4lunch.R;

/**
 * Created by Skiti on 04/03/2020
 */

public class ProfileActivity extends MainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_restaurant_profile);
        ButterKnife.bind(this);
    }

}

