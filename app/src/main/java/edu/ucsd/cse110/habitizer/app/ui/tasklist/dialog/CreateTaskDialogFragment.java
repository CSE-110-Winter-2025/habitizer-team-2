package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentCreateTaskDialogBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class CreateTaskDialogFragment extends DialogFragment {

    private FragmentCreateTaskDialogBinding view;
    private MainViewModel activityModel;
    private int routineID;

    CreateTaskDialogFragment(){
        // empty constructor
    }

    public static CreateTaskDialogFragment newInstance(int routineID) {
        CreateTaskDialogFragment fragment = new CreateTaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("ROUTINE_ID", routineID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // defines isMorning
        if (getArguments() != null) {
            routineID = getArguments().getInt("ROUTINE_ID", 0);
        }

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentCreateTaskDialogBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Add Task")
                .setMessage("Please provide the name of the task you wish to add.")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        var name = view.addTaskNameText.getText().toString();

        // depending on isMorning, the appropriate task repository is chosen
        var designatedRepo = activityModel.getActiveRoutine();
//        var designatedRepo = isMorning ? activityModel.getMorningTaskRepository() : activityModel.getEveningTaskRepository();
        var task = new Task(null, -1, name, false);

        // appends tasks to given list
        if (view.appendRadioBtn.isChecked()){
            activityModel.appendTask(task, designatedRepo);
        } else if(view.prependRadioBtn.isChecked()){
            activityModel.prependTask(task, designatedRepo);
        } else{
            throw new IllegalStateException("no radio button is checked.");
        }

        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

}
