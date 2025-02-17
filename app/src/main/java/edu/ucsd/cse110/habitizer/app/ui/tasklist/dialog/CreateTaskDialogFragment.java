package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentCreateTaskDialogBinding;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class CreateTaskDialogFragment extends DialogFragment {

    private FragmentCreateTaskDialogBinding view;
    private MainViewModel activityModel;
    private boolean isMorning;

    CreateTaskDialogFragment(){
        // empty constructor
    }

    public static CreateTaskDialogFragment newInstance(boolean isMorning) {
        CreateTaskDialogFragment fragment = new CreateTaskDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("IS_MORNING", isMorning);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // defines isMorning
        if (getArguments() != null) {
            isMorning = getArguments().getBoolean("IS_MORNING", true);
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
        var designatedRepo = isMorning ? activityModel.getMorningTaskRepository() : activityModel.getEveningTaskRepository();
        var task = new Task(null, -1, name, false);

        // appends tasks to given list
        if (view.appendRadioBtn.isChecked()){
            activityModel.append(task, designatedRepo);
        } else if(view.prependRadioBtn.isChecked()){
            activityModel.prepend(task, designatedRepo);
        } else{
            throw new IllegalStateException("no radio button is checked.");
        }

        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

}
