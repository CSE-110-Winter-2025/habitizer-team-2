package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditRoutineTasksBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmDeleteTaskDialogFragment;
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

//    public boolean isMorning;
    public int routineID;


    public EditRoutineTasksFragment() {
        // Required empty public constructor
    }

    // Initializes Fragment
    public static EditRoutineTasksFragment newInstance(int routineID) {
        EditRoutineTasksFragment fragment = new EditRoutineTasksFragment();
        Bundle args = new Bundle();
        args.putInt("ROUTINE_ID", routineID); // stores routine id to specify the given routine
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            routineID = getArguments().getInt("ROUTINE_ID", 0);
        }

        // Initialize ViewModel
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize adapter dynamically based on `isMorning`

        // Below is the updated code when default morning and evening lists are created instead of just one default
        // would have to update getOrderedTasks() method to instead two methods that return desired tasks
        var tasksData = activityModel.getOrderedTasks(routineID);

        this.adapterList = new EditTaskListAdapter(requireContext(), new ArrayList<>(), activityModel, id -> {
            var dialogFragment = ConfirmEditTaskDialogFragment.newInstance(id, routineID);
            dialogFragment.show(getParentFragmentManager(), "ConfirmEditTaskDialogFragment");
        }, id -> {
            var dialogFragment = ConfirmDeleteTaskDialogFragment.newInstance(id, routineID);
            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteTaskDialogFragment");
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


        view.saveGoalButton.setOnClickListener(v -> {
            String userInput = view.goalTimeInput.getText().toString();
            try {
                int intUserInput = Integer.parseInt(userInput);
                new AlertDialog.Builder(getContext())
                        .setTitle("Success!")
                        .setMessage("Goal time saved")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                activityModel.getRoutine(routineID).setGoalTime(intUserInput);
            } catch (NumberFormatException e) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Fail!")
                        .setMessage("Invalid number format!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                e.printStackTrace(); // Logs the error in Logcat
            }

            view.goalTimeInput.setText("");
        });





        // edit tasks list has adapter list
        view.editTasksList.setAdapter(adapterList);

        view.createTaskButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskDialogFragment.newInstance(routineID);
            dialogFragment.show(getParentFragmentManager(), "ConfirmEditTaskDialog");
        });


        return view.getRoot();
    }



}