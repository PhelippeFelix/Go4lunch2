package cfwz.skiti.go4lunch.ui.workmates;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.models.Workmate;

import static androidx.core.content.ContextCompat.startActivity;

public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesRecyclerViewAdapter.ViewHolder> {

    private final List<Workmate> mWorkmates;

    public WorkmatesRecyclerViewAdapter(List<Workmate> items) {
        mWorkmates = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmates_object_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Workmate workmate = mWorkmates.get(position);
        holder.mNeighbourName.setText(workmate.getName());
        Glide.with(holder.mWorkmateAvatar.getContext())
                .load(workmate.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mWorkmateAvatar);
    }


    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_list_avatar)
        public ImageView mWorkmateAvatar;
        @BindView(R.id.item_list_name)
        public TextView mNeighbourName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
