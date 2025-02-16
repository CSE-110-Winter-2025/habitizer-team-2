package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditRoutineTasksBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmEditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

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

//        tasksData.observe(tasks -> {
//            if (tasks == null) return;
//            if (adapterList == null) {
//                adapterList = new EditTaskListAdapter(requireContext(), new ArrayList<>(tasks), activityModel, id -> {
//                    var dialogFragment = ConfirmEditTaskDialogFragment.newInstance(id, isMorning);
//                    dialogFragment.show(getParentFragmentManager(), "ConfirmEditTaskDialogFragment");
//                });
//            } else {
//                adapterList.clear();
//                adapterList.addAll(new ArrayList<>(tasks));
//                adapterList.notifyDataSetChanged();
//            }
//        });
        this.adapterList = new EditTaskListAdapter(requireContext(), new ArrayList<>(), activityModel, id -> {
            var dialogFragment = ConfirmEditTaskDialogFragment.newInstance(id, isMorning);
            dialogFragment.show(getParentFragmentManager(), "ConfirmEditTaskDialogFragment");
        });

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
        this.view = FragmentEditRoutineTasksBinding.inflate(inflater, container, false);


        // edit tasks list has adapter list
        view.editTasksList.setAdapter(adapterList);

        view.createTaskButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskDialogFragment.newInstance(isMorning);
            dialogFragment.show(getParentFragmentManager(), "ConfirmEditTaskDialog");
        });


        return view.getRoot();
    }


}