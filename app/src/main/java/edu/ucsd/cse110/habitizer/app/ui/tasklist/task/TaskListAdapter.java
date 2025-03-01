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
    boolean isMorning;
    Stopwatch stopwatch;
    int taskStartTime = 0;

    private Runnable endRoutineCallback;
    public TaskListAdapter(Context context,
                                  List<Task> tasks, MainViewModel activityModel,
                           boolean isMorning, Runnable endRoutineCallback) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(tasks));
        this.activityModel = activityModel;
        this.isMorning = isMorning;
        this.endRoutineCallback = endRoutineCallback;
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
            if(isMorning){
                activityModel.checkOff(task.id(), activityModel.getMorningTaskRepository());
            } else {
                activityModel.checkOff(task.id(), activityModel.getEveningTaskRepository());
            }
            binding.timeComplete.setText(timeCompleted);
            notifyDataSetChanged();

            if (allTasksCompleted()) {
                if (endRoutineCallback != null) {
                    endRoutineCallback.run();
                }
            }
            binding.taskBox.setEnabled(false);
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

    private boolean allTasksCompleted() {
        for (int i = 0; i < getCount(); i++) {
            Task task = getItem(i);
            if (task != null && !task.checkedOff()) {
                return false;
            }
        }
        return true;
    }
}
