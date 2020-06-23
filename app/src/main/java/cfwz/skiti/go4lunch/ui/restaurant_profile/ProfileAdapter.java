package cfwz.skiti.go4lunch.ui.restaurant_profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.Workmate;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
        private List<Workmate> mWorkmates;


        public ProfileAdapter(List<Workmate> result) {
            this.mWorkmates = result;
        }

        @Override
        public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.activity_restaurant_profile_object_list, parent,false);
            return new ProfileViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProfileViewHolder viewHolder, int position) {
            viewHolder.updateWithData(this.mWorkmates.get(position));
        }

        @Override
        public int getItemCount() {
            int itemCount = 0;
            if (mWorkmates != null) itemCount = mWorkmates.size();
            return itemCount;
        }
    }