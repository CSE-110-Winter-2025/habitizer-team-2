package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineListBinding;

public class RoutineListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentRoutineListBinding view;
    private RoutineListAdapter adapter;

    public RoutineListFragment() {
        // Required empty public constructor
    }

    public static RoutineListFragment newInstance() {
        RoutineListFragment fragment = new RoutineListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter (with an empty list for now)
        this.adapter = new RoutineListAdapter(requireContext(), List.of(), this);

        var routinesData = activityModel.getOrderedRoutines();


        routinesData.observe(routines -> {
            if (routines == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(routines)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });

    }

    public MainViewModel getActivityModel() {return this.activityModel;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = FragmentRoutineListBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.routineList.setAdapter(adapter);

        return view.getRoot();
    }

    /**
     * Called when the view hierarchy associated with the fragment is being removed.
     * This method is used to perform any final cleanup before the fragment's view is destroyed.
     *
     */
    public void onDestroyView() {
        super.onDestroyView();
    }



}
