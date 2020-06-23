package cfwz.skiti.go4lunch.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.googleplaces.ResultDetails;


public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private List<ResultDetails> mResultDetails;
    private String mLocation;

    public ListRecyclerViewAdapter(List<ResultDetails> items, String location) {
        mResultDetails = items;
        mLocation = location;}

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_object_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.updateWithData(this.mResultDetails.get(position), this.mLocation);
    }

    public ResultDetails getRestaurantDetails(int position){
        return this.mResultDetails.get(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mResultDetails != null) itemCount = mResultDetails.size();
        return itemCount;
    }
}