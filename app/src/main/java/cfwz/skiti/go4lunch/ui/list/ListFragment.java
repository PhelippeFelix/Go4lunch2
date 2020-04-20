package cfwz.skiti.go4lunch.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.api.UserHelper;
import cfwz.skiti.go4lunch.models.Workmate;

public class ListFragment extends Fragment {

    private List<Workmate> mWorkmates;
    private RecyclerView mRecyclerView;



    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initList();
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        //mWorkmates = UserHelper.getAllWorkmates();
       // mRecyclerView.setAdapter(new ListRecyclerViewAdapter(mWorkmates));
    }


    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
}