package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.DialogFragment;

import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.EditListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;


public class EditTaskListAdapter extends ArrayAdapter<Task> {
    private final MainViewModel activityModel;
    private final int routineID; // Add this line to store routine ID
    Consumer<Integer> onEditClick;
    Consumer<Integer> onDeleteClick;

    // Parameters are tasks, if it is morning routine selected, and activity model
    public EditTaskListAdapter(Context context, List<Task> tasks, MainViewModel activityModel, int routineID, Consumer<Integer> onEditClick, Consumer<Integer> onDeleteClick) {
        super(context, 0, tasks);
        this.activityModel = activityModel;
        this.routineID = routineID;
        this.onEditClick = onEditClick;
        this.onDeleteClick = onDeleteClick;
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

        binding.taskEditButton.setOnClickListener(v -> {
            var id = task.id();
            assert id != null;
            onEditClick.accept(id);
        });

        binding.deleteTaskBtn.setOnClickListener(v -> {
            var id = task.id();
            assert id != null;
            onDeleteClick.accept(id);
        });

//        //binding to up and down arrows for modified task positions
//        binding.btnMoveUp.setOnClickListener(v -> {
//            if (position > 0) { // Prevent moving first task up (KEEP THIS)
//                activityModel.moveTask(task.id(), routineID, true);
//                notifyDataSetChanged();
//            }
//        });
//
//        binding.btnMoveDown.setOnClickListener(v -> {
//            if (position < getCount() - 1) { // Prevent moving last task down (KEEP THIS)
//                activityModel.moveTask(task.id(), routineID, false);
//                notifyDataSetChanged();
//            }
//        });

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
