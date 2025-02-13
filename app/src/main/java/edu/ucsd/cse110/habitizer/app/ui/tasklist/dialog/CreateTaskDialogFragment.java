//package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;
//
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//import edu.ucsd.cse110.habitizer.app.MainViewModel;
//import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogCreateCardBinding;
//import edu.ucsd.cse110.habitizer.lib.domain.Task;
//
//public class CreateTaskDialogFragment extends DialogFragment {
//
//    private FragmentDialogCreateTaskBinding view;
//
//    private MainViewModel activityModel;
//
//    CreateTaskDialogFragment(){
//
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//
//        var modelOwner = requireActivity();
//        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
//        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
//        this.activityModel = modelProvider.get(MainViewModel.class);
//    }
//
//    public static CreateTaskDialogFragment newInstance(){
//        var fragment = new CreateTaskDialogFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        this.view = FragmentDialogCreateTaskBinding.inflate(getLayoutInflater());
//
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("New Card")
//                .setMessage("Please provide the new card text.")
//                .setView(view.getRoot())
//                .setPositiveButton("Create", this::onPositiveButtonClick)
//                .setNegativeButton("Cancel", this::onNegativeButtonClick)
//                .create();
//    }
//
//    private void onPositiveButtonClick(DialogInterface dialog, int which){
//        var name = view.taskNameEditText.getText().toString();
//
//        var task = new Task(null, -1, name);
//
//        if (view.appendRadioButton.isChecked()){
//            activityModel.append(task);
//        } else if(view.prependRadioButton.isChecked()){
//            activityModel.prepend(task);
//        } else{
//            throw new IllegalStateException("no radio button is checked.");
//        }
//
//        dialog.dismiss();
//    }
//
//    private void onNegativeButtonClick(DialogInterface dialog, int which){
//        dialog.cancel();
//    }
//
//}
