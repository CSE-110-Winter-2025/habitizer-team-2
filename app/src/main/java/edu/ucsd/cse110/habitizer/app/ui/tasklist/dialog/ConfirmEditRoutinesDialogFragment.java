package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentConfirmEditRoutinesDialogBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentConfirmEditTaskDialogBinding;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemEditRoutineBinding;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemRoutineBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class ConfirmEditRoutinesDialogFragment extends DialogFragment {
    private FragmentConfirmEditRoutinesDialogBinding view;
    private ListItemEditRoutineBinding binding;
    private MainViewModel activityModel;
    private static final String ARG_TASK_ID = "task_id";
    private static final String ARG_ROUTINE_ID = "routine_id";
    private List<String> routineNames;
    private List<Integer> routineIDs;
    private int routineIndex = -1;


//    private int taskID;
//    private int routineID;

    public ConfirmEditRoutinesDialogFragment() {
        // Required empty public constructor
    }

    public static ConfirmEditRoutinesDialogFragment newInstance(ArrayList<String> routineNames, ArrayList<Integer> routineIDs) {
        var fragment = new ConfirmEditRoutinesDialogFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_TASK_ID, routineID);
//        args.putInt(ARG_TASK_ID, taskID);
        args.putStringArrayList("ROUTINE_NAMES", routineNames);
        args.putIntegerArrayList("ROUTINE_IDS", routineIDs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        this.taskID = requireArguments().getInt(ARG_TASK_ID);
//        this.routineID = requireArguments().getInt(ARG_ROUTINE_ID);
        this.routineNames = requireArguments().getStringArrayList("ROUTINE_NAMES");
        this.routineIDs = requireArguments().getIntegerArrayList("ROUTINE_IDS");

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        return new AlertDialog.Builder(requireContext())
                .setTitle("Select A Routine to Edit")
                .setMessage("Select one of the following routines.")

                .setPositiveButton("Select", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
//        var designatedRepo = activityModel.getRoutine(routineID);
//        activityModel.removeTask(taskID, designatedRepo);
        int routineID = routineIDs.get(routineIndex);
        openEditRoutineTasksFragment(routineID);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_edit_routines_dialog, container, false);
    }

    private void openEditRoutineTasksFragment(int routineID) {
        var editFragment = EditRoutineTasksFragment.newInstance(routineID);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, editFragment)
                .addToBackStack(null)
                .commit();
    }



}
