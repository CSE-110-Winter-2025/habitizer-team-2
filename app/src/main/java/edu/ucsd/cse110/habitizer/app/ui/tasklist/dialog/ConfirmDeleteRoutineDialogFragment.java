package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;

public class ConfirmDeleteRoutineDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private static final String ARG_ROUTINE_ID = "routine_id";

    private int routineID;

    ConfirmDeleteRoutineDialogFragment(){}

    public static ConfirmDeleteRoutineDialogFragment newInstance(int routineID){
        var fragment = new ConfirmDeleteRoutineDialogFragment();
        Bundle args = new Bundle();
        args.putInt("ROUTINE_ID", routineID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
                .setTitle("Delete Routine")
                .setMessage("Are you sure you want to delete this routine?")
                .setPositiveButton("Delete", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        try{
            activityModel.removeRoutine(routineID, activityModel.getRoutineRepository());
        } catch(Exception e){
            Log.e("DeleteRoutine", "Error d");
        }finally {
            dialog.dismiss();
        }
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_confirm_delete_routine_dialog, container, false);
    }
}
