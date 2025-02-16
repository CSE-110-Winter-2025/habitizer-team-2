package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.Stopwatch;

public class TaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private TaskListAdapter adapter;

    public boolean isMorning;

    public int elapsedTimeMinutes;
    private Runnable updateRunnable;
    private Handler handler;
    private TextView elapsedTimeTextView;
    private Stopwatch stopwatch;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(boolean isMorning) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putBoolean("IS_MORNING", isMorning);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            isMorning = getArguments().getBoolean("IS_MORNING", true);
            elapsedTimeMinutes = getArguments().getInt("ELAPSED_TIME_MINUTES", 0);
        }
        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter (with an empty list for now)
        this.adapter = new TaskListAdapter(requireContext(), List.of(), activityModel, isMorning);

        var tasksData = isMorning ? activityModel.getMorningOrderedTasks() : activityModel.getEveningOrderedTasks();
        tasksData.observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });

        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.taskList.setAdapter(adapter);

        // Getting the elapsedTime text from layout
        elapsedTimeTextView = view.elapsedTimeTextView;

        // Creating a new stopwatch object and passing in elapsedTimeTextView to update it with minutes
        stopwatch = new Stopwatch(view.elapsedTimeTextView);

        // Start the Stopwatch
        stopwatch.start();

        view.stopButton.setOnClickListener( v -> {
                if(stopwatch.isRunning){
                    stopwatch.stop();
                }
        });

        view.playButton.setOnClickListener( v -> {
            if (!stopwatch.isRunning) {
                stopwatch.start();
            }

        });

        view.ffButton.setOnClickListener(v -> {
            stopwatch.fastforward(30);
        });

        return view.getRoot();
    }

    public boolean getIsMorning(){return this.isMorning;}

    /**
     * Called when the view hierarchy associated with the fragment is being removed.
     * This method is used to perform any final cleanup before the fragment's view is destroyed.
     *
     */
    public void onDestroyView() {
        super.onDestroyView();
        stopwatch.stop();
    }
}
