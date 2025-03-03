package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomepageRoutineBinding;

import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmDeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmEditRoutinesDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmEditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditTaskListAdapter;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListAdapter;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;


public class HomePageRoutineFragment extends Fragment {
    private FragmentHomepageRoutineBinding view;
    private MainViewModel activityModel;

    private HomePageRoutineListAdapter adapter;


    public HomePageRoutineFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomePageRoutineFragment newInstance() {
        HomePageRoutineFragment fragment = new HomePageRoutineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter (with an empty list for now)

        var routinesData = activityModel.getOrderedRoutines();

//        this.adapter = new HomePageRoutineListAdapter(requireContext(), new ArrayList<>(), this, activityModel);
//        this.adapter = new HomePageRoutineListAdapter(requireContext(), new ArrayList<>(), this, activityModel, routineNames -> {
//            var dialogFragment = ConfirmEditRoutinesDialogFragment.newInstance(routineNames);
//            dialogFragment.show(getParentFragmentManager(), "ConfirmEditRoutinesDialogFragment");
//        });

        this.adapter = new HomePageRoutineListAdapter(requireContext(), new ArrayList<>(), this, activityModel);

        // Observe routines and update adapter
        activityModel.getOrderedRoutines().observe(routines -> {
            if (routines == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(routines));
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_routine, container, false);
        ListView routineListView = view.findViewById(R.id.routine_list);
        routineListView.setAdapter(adapter);

        Button editRoutineButton = view.findViewById(R.id.edit_routines_button);
        editRoutineButton.setOnClickListener(v -> showEditRoutineDialog());

        return view.getRootView();
    }

    private void showEditRoutineDialog() {
        // Get all routine names
        ArrayList<String> routineNames = new ArrayList<>();
        ArrayList<Integer> routineIDs = new ArrayList<>();

        for (int i = 0; i < adapter.getCount(); i++) {
            Routine routine = adapter.getItem(i);
            if (routine != null) {
                routineNames.add(routine.name());
                routineIDs.add(routine.id());
            }
        }

        Log.d("HomePageRoutineFragment", "Passing routines to dialog: " + routineNames);

        var dialogFragment = ConfirmEditRoutinesDialogFragment.newInstance(routineNames, routineIDs);
        dialogFragment.show(getParentFragmentManager(), "ConfirmEditRoutinesDialogFragment");
    }



    public MainViewModel getActivityModel() {
        return this.activityModel;
    }
}