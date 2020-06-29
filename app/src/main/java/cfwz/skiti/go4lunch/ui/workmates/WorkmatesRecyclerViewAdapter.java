package cfwz.skiti.go4lunch.ui.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.Workmate;


public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {
    private List<Workmate> mWorkmates;


    public WorkmatesRecyclerViewAdapter(List<Workmate> items) { mWorkmates = items; }


    @Override
    public WorkmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmates_object_list, parent, false);
        return new WorkmatesViewHolder(view); }


    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        holder.updateWithData(this.mWorkmates.get(position)); }


    public Workmate getWorkmates(int position){ return this.mWorkmates.get(position); }


    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mWorkmates != null) itemCount = mWorkmates.size();
        return itemCount; }
}
