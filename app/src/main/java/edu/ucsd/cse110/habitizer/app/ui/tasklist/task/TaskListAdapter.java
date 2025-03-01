package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskListAdapter extends ArrayAdapter<Task> {
    MainViewModel activityModel;

    TaskListFragment fragment;

    int routineID;
    Stopwatch stopwatch;
    int taskStartTime = 0;
    public TaskListAdapter(Context context,
                                  List<Task> tasks, TaskListFragment fragment,
                           int routineID) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(tasks));
        this.fragment = fragment;
        this.activityModel = fragment.getActivityModel();
        this.routineID = routineID;
    }

    public void setStopwatch (Stopwatch stopwatch) {
        this.stopwatch = stopwatch;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the task for this position.
        var task = getItem(position);


        assert task != null;

        // Check if a view is being reused...
        ListItemTaskBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        // Populate the view with the task's data.
        binding.taskName.setText(task.name());


        binding.taskBox.setOnClickListener(b -> {
            int completedTime = stopwatch.getElapsedTimeSeconds();
            int timeElapsed = (completedTime - taskStartTime) / 60 + 1;
            taskStartTime = completedTime;

            String timeCompleted = "[" + timeElapsed + " m]";

            // activityModel.getRoutine(routineID)
            activityModel.checkOff(task.id(), routineID);

            binding.timeComplete.setText(timeCompleted);
            notifyDataSetChanged();

        });

        if(task.checkedOff()){
            binding.taskImg.setImageResource(R.drawable.silvringchecked);
        } else {
            binding.taskImg.setImageResource(R.drawable.silvring);
        }

        return binding.getRoot();
    }

    // The below methods aren't strictly necessary, usually.
    // But get in the habit of defining them because they never hurt
    // (as long as you have IDs for each item) and sometimes you need them.

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var task = getItem(position);
        assert task != null;

        var id = task.id();
        assert id != null;

        return id;
    }

}
