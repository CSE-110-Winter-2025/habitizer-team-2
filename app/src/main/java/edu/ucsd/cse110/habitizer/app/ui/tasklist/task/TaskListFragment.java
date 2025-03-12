package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;

public class TaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private TaskListAdapter adapter;

    public int routineID;
    public int elapsedTimeMinutes;

    private Runnable updateRunnable;
    private Handler handler;
    private TextView elapsedTimeTextView;

    private Stopwatch stopwatchTask;


    private Stopwatch stopwatch;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(int routineID) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt("ROUTINE_ID", routineID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            routineID = getArguments().getInt("ROUTINE_ID", 0);
            elapsedTimeMinutes = getArguments().getInt("ELAPSED_TIME_MINUTES", 0);
        }
        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        activityModel.setActiveRoutine(routineID);

        // Initialize the Adapter (with an empty list for now)
        this.adapter = new TaskListAdapter(requireContext(), List.of(), activityModel, routineID, getEndButtonCallback());


        var tasksData = activityModel.getOrderedTasks();

        tasksData.observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.taskList.setAdapter(adapter);

        // activityModel.getRoutine(routineID)
        activityModel.getGoalTimeText().observe(view.goalTextView::setText);

        //activityModel.getRoutine(name)
        activityModel.getRoutineName().observe(view.toolbarTitle::setText);



        // Getting the elapsedTime text from layout
        elapsedTimeTextView = view.elapsedTimeTextView;

        // Creating a new stopwatch object and passing in elapsedTimeTextView to update it with minutes
        stopwatch = new Stopwatch(view.elapsedTimeTextView);
        stopwatchTask = new Stopwatch(view.elapsedTaskTimeTextView); //added for taskTime

        // End Routine Button;
        Button endButton = view.getRoot().findViewById((R.id.end_button));
        endButton.setOnClickListener(v -> {
            view.elapsedTimeTextView.setText(String.valueOf(stopwatch.getElapsedTimeInMinutes()+1));
            view.elapsedTaskTimeTextView.setText(String.valueOf(0)); //edge case when end routine is done (set to 0)
            stopwatch.stop();
            stopwatchTask.stop();
            disableInteractions();
        });

        // Start the Stopwatch
        stopwatch.start();
        stopwatchTask.start();

        // Give stopwatch access to adapter
        adapter.setStopwatch(stopwatch);
        adapter.setStopwatchTask(stopwatchTask);

        view.timeButton.setOnClickListener( v -> { //added for task time as well
                if(stopwatch.isRunning) {
                    stopwatch.stop();
                    stopwatchTask.stop();
                    view.timeButton.setImageResource(R.drawable.playbutton);
                }else{
                    stopwatch.start();
                    stopwatchTask.start();
                    view.timeButton.setImageResource(R.drawable.stopbutton);
                }
        });

        view.ffButton.setOnClickListener(v -> {
            stopwatch.fastforward(15);
            stopwatchTask.fastforward(15);
        });


        return view.getRoot();
    }

//    public boolean getIsMorning(){return this.isMorning;}

    public Runnable getEndButtonCallback(){
        Runnable endRoutineCallback = () -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Button endButton = getView().findViewById(R.id.end_button);
                    if (endButton != null) {
                        endButton.performClick();
                    }
                });
            }
        };
        return endRoutineCallback;
    }

    /**
     * Called when the view hierarchy associated with the fragment is being removed.
     * This method is used to perform any final cleanup before the fragment's view is destroyed.
     *
     */
    public void onDestroyView() {
        super.onDestroyView();
        stopwatch.stop();
        activityModel.uncheckTasks();
        stopwatchTask.stop();
    }


    private void disableInteractions() {
        view.taskList.setEnabled(false);
        for (int i = 0; i < view.taskList.getChildCount(); i++) {
            View listItem = view.taskList.getChildAt(i);
            listItem.setEnabled(false);
        }
        view.endButton.setEnabled(false);
        view.ffButton.setEnabled(false);
        view.timeButton.setEnabled(false);
    }
}
