package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditRoutineTasksBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;

// TO DO: Make two lists for morning and evening -> update methods to return those lists -> update edit list

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditRoutineTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditRoutineTasksFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private MainViewModel activityModel;
    private FragmentEditRoutineTasksBinding view;

    // adapter for Edit List & stores a boolean to see if morning routine is selected
    private EditTaskListAdapter adapterList;

    public boolean isMorning;


    public EditRoutineTasksFragment() {
        // Required empty public constructor
    }

    // Initializes Fragment
    public static EditRoutineTasksFragment newInstance(boolean isMorning) {
        EditRoutineTasksFragment fragment = new EditRoutineTasksFragment();
        Bundle args = new Bundle();
        args.putBoolean("IS_MORNING", isMorning); // stores boolean value to specify which data to use
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isMorning = getArguments().getBoolean("IS_MORNING", true);
        }

        // Initialize ViewModel
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize adapter dynamically based on `isMorning`

        // Below is the updated code when default morning and evening lists are created instead of just one default
        // would have to update getOrderedTasks() method to instead two methods that return desired tasks
        var tasksData = isMorning ? activityModel.getMorningOrderedTasks() : activityModel.getEveningOrderedTasks();
        adapterList = new EditTaskListAdapter(requireContext(), new ArrayList<>(), activityModel);

        // tasksData.observe(tasks -> {
        tasksData.observe(tasks -> {
            if (tasks == null) return;
            adapterList.clear();
            adapterList.addAll(new ArrayList<>(tasks));
            adapterList.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = FragmentEditRoutineTasksBinding.inflate(inflater, container, false);
        // edit tasks list has adapter list
        view.editTasksList.setAdapter(adapterList);
        return view.getRoot();
    }
}