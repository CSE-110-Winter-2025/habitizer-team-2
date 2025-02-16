package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentConfirmEditTaskDialogBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentCreateTaskDialogBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentCreateTaskDialogBinding;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmEditTaskDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmEditTaskDialogFragment extends DialogFragment {

    private FragmentConfirmEditTaskDialogBinding view;
    private MainViewModel activityModel;
    private static final String ARG_TASK_ID = "task_id";
    private boolean isMorning;
    private int taskID;

    public ConfirmEditTaskDialogFragment() {
        // Required empty public constructor
    }

    public static ConfirmEditTaskDialogFragment newInstance(int taskID, boolean isMorning) {
        ConfirmEditTaskDialogFragment fragment = new ConfirmEditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("IS_MORNING", isMorning);
        args.putInt(ARG_TASK_ID, taskID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.taskID = requireArguments().getInt(ARG_TASK_ID);
        this.isMorning = requireArguments().getBoolean("IS_MORNING");

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentConfirmEditTaskDialogBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(requireContext())
                .setTitle("Rename Task")
                .setMessage("Please provide a new name for this task.")
                .setView(view.getRoot())
                .setPositiveButton("Rename", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        var newName = view.renameTaskText.getText().toString();

        // depending on isMorning, the appropriate task repository is chosen
        var designatedRepo = isMorning ? activityModel.getMorningTaskRepository() : activityModel.getEveningTaskRepository();

        activityModel.rename(taskID, newName, designatedRepo);

        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_edit_task_dialog, container, false);
    }
}