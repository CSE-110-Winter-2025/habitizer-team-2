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

/**
 * A DialogFragment that confirms the deletion of a routine.
 * This fragment displays a confirmation dialog to the user,
 * asking if they are sure they want to delete the selected routine.
 */
public class ConfirmDeleteRoutineDialogFragment extends DialogFragment {
    private MainViewModel activityModel;
    private static final String ARG_ROUTINE_ID = "routine_id";
    private int routineID;

    /**
     * Default constructor for ConfirmDeleteRoutineDialogFragment.
     */
    ConfirmDeleteRoutineDialogFragment(){}

    /**
     * Creates a new instance of ConfirmDeleteRoutineDialogFragment with the specified routine ID.
     *
     * @param routineID The ID of the routine to be deleted.
     * @require routineID >= 0
     * @return A new instance of ConfirmDeleteRoutineDialogFragment.
     */
    public static ConfirmDeleteRoutineDialogFragment newInstance(int routineID){
        var fragment = new ConfirmDeleteRoutineDialogFragment();
        Bundle args = new Bundle();
        args.putInt("ROUTINE_ID", routineID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of the fragment.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.routineID = requireArguments().getInt("ROUTINE_ID");

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    /**
     * Called to create the dialog.
     *
     * @param savedInstanceState If the dialog is being re-created from a previous saved state, this is the state.
     * @require savedInstanceState != null
     * @return A new AlertDialog instance.
     */
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

    /**
     * Handles the positive button click event.
     * Removes the routine from the repository.
     *
     * @param dialog The dialog that received the click.
     * @param which The button that was clicked.
     * @require dialog != null
     */
    private void onPositiveButtonClick(DialogInterface dialog, int which){
        try{
            activityModel.removeRoutine(routineID, activityModel.getRoutineRepository());
        } catch(Exception e){
            Log.e("DeleteRoutine", "Error d");
        }finally {
            dialog.dismiss();
        }
    }

    /**
     * Handles the negative button click event.
     * Cancels the dialog.
     *
     * @param dialog The dialog that received the click.
     * @param which The button that was clicked.
     * @require dialog != null
     */
    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     * @require inflater != null && container != null && savedInstanceState != null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_confirm_delete_routine_dialog, container, false);
    }
}
