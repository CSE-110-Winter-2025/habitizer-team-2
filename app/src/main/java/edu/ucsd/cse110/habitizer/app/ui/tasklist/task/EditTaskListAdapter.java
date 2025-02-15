package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.EditListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;


public class EditTaskListAdapter extends ArrayAdapter<Task> {
    private final MainViewModel activityModel;

    // Parameters are tasks, if it is morning routine selected, and activity model
    public EditTaskListAdapter(Context context, List<Task> tasks, MainViewModel activityModel) {
        super(context, 0, tasks);
        this.activityModel = activityModel;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var task = getItem(position);
        assert task != null;

        // Check if a view is being reused...
        // Binding for EditListItemBinding to bind data to Edit List
        EditListItemTaskBinding binding;
        if (convertView == null) {
            // Inflate new view if convertView is null
            var layoutInflater = LayoutInflater.from(getContext());
            binding = EditListItemTaskBinding.inflate(layoutInflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding); // Store binding in tag for reuse
        } else {
            // Reuse binding if convertView is available
            binding = (EditListItemTaskBinding) convertView.getTag();
        }

        // Populate the view with task data
        binding.editListTaskName.setText(task.name());

        return convertView;
    }

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
