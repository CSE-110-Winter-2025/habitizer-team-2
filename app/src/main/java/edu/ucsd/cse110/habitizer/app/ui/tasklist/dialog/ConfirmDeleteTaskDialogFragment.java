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
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentConfirmEditTaskDialogBinding;

public class ConfirmDeleteTaskDialogFragment extends DialogFragment {
    private FragmentConfirmEditTaskDialogBinding view;
    private MainViewModel activityModel;
    private static final String ARG_TASK_ID = "task_id";

    private int taskID;
    private int routineID;

    public ConfirmDeleteTaskDialogFragment() {
        // Required empty public constructor
    }

    public static ConfirmDeleteTaskDialogFragment newInstance(int taskID, int routineID) {
        var fragment = new ConfirmDeleteTaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("ROUTINE_ID", routineID);
        args.putInt(ARG_TASK_ID, taskID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.taskID = requireArguments().getInt(ARG_TASK_ID);
        this.routineID = requireArguments().getInt("ROUTINE_ID");

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        return new AlertDialog.Builder(requireContext())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        try {
            var designatedRepo = activityModel.getRoutine(routineID);
            Log.d("designated Repo", activityModel.getRoutine(routineID).id().toString());
            activityModel.removeTask(taskID, designatedRepo);
        } catch (Exception e) {
            Log.e("DeleteTask", "Error deleting task: " + e.getMessage(), e);
        } finally {
            dialog.dismiss();
        }
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_delete_task_dialog, container, false);
    }



}
