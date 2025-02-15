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
<<<<<<< HEAD:app/src/main/java/edu/ucsd/cse110/habitizer/app/ui/tasklist/task/TaskListAdapterEvening.java
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskEveningBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskListEveningBinding;
=======
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
>>>>>>> master:app/src/main/java/edu/ucsd/cse110/habitizer/app/ui/tasklist/task/TaskListEveningAdapter.java
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskListEveningAdapter extends ArrayAdapter<Task> {
    MainViewModel activityModel;
    public TaskListEveningAdapter(Context context,
                                  List<Task> tasks, MainViewModel activityModel) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(tasks));
        this.activityModel = activityModel;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the task for this position.
        var task = getItem(position);
        assert task != null;

        // Check if a view is being reused...
        ListItemTaskEveningBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemTaskEveningBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskEveningBinding.inflate(layoutInflater, parent, false);
        }

        // Populate the view with the task's data.
        binding.taskName.setText(task.name());

        binding.taskName.setOnClickListener(b -> {
            activityModel.checkOff(task.id(), activityModel.getEveningTaskRepository());
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
