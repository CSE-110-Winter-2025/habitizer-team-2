//package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import edu.ucsd.cse110.habitizer.app.MainViewModel;
//
//public class ConfirmDeleteTaskDialogFragment extends DialogFragment {
//    private static final String ARG_FLASHCARD_ID = "flashcard_id";
//    private int flashcardID;
//    private MainViewModel activityModel;
//
//    //empty constructor
//    ConfirmDeleteTaskDialogFragment() {
//
//    }
//
//    public static ConfirmDeleteTaskDialogFragment newInstance(int flashcardID) {
//        var fragment = new ConfirmDeleteTaskDialogFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_FLASHCARD_ID, flashcardID);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        this.flashcardID = requireArguments().getInt(ARG_FLASHCARD_ID);
//
//        var modelOwner = requireActivity();
//        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
//        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
//        this.activityModel = modelProvider.get(MainViewModel.class);
//
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
//        return new AlertDialog.Builder(requireContext())
//                .setTitle("Delete Task")
//                .setMessage("Are you sure you want to delte this card?")
//                .setPositiveButton("Delete", this::onPositiveButtonClick)
//                .setNegativeButton("Cancel", this::onNegativeButtonClick)
//                .create();
//    }
//
//    private void onPositiveButtonClick(DialogInterface dialog, int which){
//        activityModel.remove(flashcardID);
//        dialog.dismiss();
//    }
//
//    private void onNegativeButtonClick(DialogInterface dialog, int which){
//        dialog.cancel();
//    }
//
//}
