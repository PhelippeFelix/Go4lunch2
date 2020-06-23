package cfwz.skiti.go4lunch.ui.settings;

import android.app.AlarmManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.api.UserHelper;
import cfwz.skiti.go4lunch.ui.map.MapViewModel;
import cfwz.skiti.go4lunch.utils.notifications.NotificationHelper;


public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;
    @BindView(R.id.settings_switch) Switch mSwitch;
    @BindView(R.id.settings_save) Button mButtonSave;

    private NotificationHelper mNotificationHelper;
    protected MapViewModel mViewModel;
    public static final long INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        this.configureToolbar();
        this.retrieveUserSettings();
        this.setListenerAndFilters();
        this.createNotificationHelper();
    }

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void retrieveUserSettings(){
        UserHelper.getWorkmatesCollection().document(getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.e("TAG", "Current data: " + documentSnapshot.getData());
                    if (documentSnapshot.getData().get("notification").equals(true)){
                        mSwitch.setChecked(true);
                        mNotificationHelper.scheduleRepeatingNotification();
                    }else{
                        mSwitch.setChecked(false);
                        mNotificationHelper.cancelAlarmRTC();
                    }
                } else {
                    Log.e("TAG", "Current data: null");
                }
            }
        });
    }

    private void setListenerAndFilters(){
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> { });
    }

    private void createNotificationHelper(){
        mNotificationHelper = new NotificationHelper(getBaseContext());
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    @OnClick(R.id.settings_save)
    public void saveSettings(){
        boolean error = false;
        if (mSwitch.isChecked()){
            mNotificationHelper.scheduleRepeatingNotification();
        }else{
            mNotificationHelper.cancelAlarmRTC();
        }
        if (!(error)){
            UserHelper.updateUserSettings(getCurrentUser().getUid(),mSwitch.isChecked()).addOnSuccessListener(
                    updateTask ->{
                        Log.e("SETTINGS_ACTIVITY", "saveSettings: DONE" );
                        Toast.makeText(this, getResources().getString(R.string.settings_save_ok), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}

