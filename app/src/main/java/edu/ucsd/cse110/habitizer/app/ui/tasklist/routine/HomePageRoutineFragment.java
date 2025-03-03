package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomepageRoutineBinding;

import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmDeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmEditRoutinesDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmEditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditTaskListAdapter;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListAdapter;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;


public class HomePageRoutineFragment extends Fragment {

    private boolean deleteMode = false;
    private boolean editMode = false;

    private FragmentHomepageRoutineBinding view;
    private MainViewModel activityModel;

    private HomePageRoutineListAdapter adapter;

    public HomePageRoutineFragment() {}

    private void deleteMode(TextView menuDescription, Button createRoutineButton){
        Log.d("Delete Button", "Deleting is working");
        deleteMode = !deleteMode;
        if (deleteMode){
            menuDescription.setText("Tap one of the routines below to delete.");
            createRoutineButton.setText("Cancel");
        }else{
            menuDescription.setText("Tap one of the routines below to get started.");
            createRoutineButton.setText("Create a Routine");
        }

        adapter.setDeleteMode(deleteMode);
    }
    private void editMode(TextView menuDescription, Button createRoutineButton){
        Log.d("Edit Button", "Editing is working");
        editMode = !editMode;

        if (editMode){
            menuDescription.setText("Tap one of the routines below to edit.");
            createRoutineButton.setText("Cancel");
        }else{
            menuDescription.setText("Tap one of the routines below to get started.");
            createRoutineButton.setText("Create a Routine");
        }
        adapter.setEditMode(editMode);
    }

    // TODO: Rename and change types and number of parameters
    public static HomePageRoutineFragment newInstance() {
        HomePageRoutineFragment fragment = new HomePageRoutineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);


        // Initialize the Adapter (with an empty list for now)

        var routinesData = activityModel.getOrderedRoutines();


        this.adapter = new HomePageRoutineListAdapter(requireContext(), new ArrayList<>(), this, activityModel);

        // Observe routines and update adapter
        routinesData.observe(routines -> {
            if (routines == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(routines));
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_routine, container, false);
        ListView routineListView = view.findViewById(R.id.routine_list);
        routineListView.setAdapter(adapter);
        ImageButton editRoutineButton = view.findViewById(R.id.routine_edit_btn);
        ImageButton deleteRoutineButton = view.findViewById(R.id.delete_routine_btn);
        Button createRoutineButton = view.findViewById(R.id.create_routine_btn);
        TextView menuDescription = view.findViewById(R.id.menu_description);

            editRoutineButton.setOnClickListener(v -> {
                if(!deleteMode){
                    editMode(menuDescription,createRoutineButton);
                }

            });

            deleteRoutineButton.setOnClickListener(v -> {
                if(!editMode){
                    deleteMode(menuDescription, createRoutineButton);
                }
            });

            createRoutineButton.setOnClickListener(v -> {
                if(deleteMode){
                    deleteMode(menuDescription, createRoutineButton);
                }

                if(editMode){
                    editMode(menuDescription, createRoutineButton);
                }else{

                }
            });

        return view.getRootView();
    }

    public MainViewModel getActivityModel() {
        return this.activityModel;
    }
}