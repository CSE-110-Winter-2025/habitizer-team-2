package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.EditListItemTaskBinding;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemEditRoutineBinding;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemRoutineBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.ConfirmDeleteRoutineDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import android.content.res.ColorStateList;

public class HomePageRoutineListAdapter extends ArrayAdapter<Routine> {
    MainViewModel activityModel;
    HomePageRoutineFragment fragment;
    private boolean deleteMode = false;
    private boolean editMode = false;

    /**
     * Sets the delete mode for the adapter.
     * When delete mode is enabled, clicking on a routine will prompt for deletion.
     *
     * @param deleteMode True to enable delete mode, false to disable.
     */
    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        notifyDataSetChanged(); // This refreshes all visible items
    }

    /**
     * Sets the edit mode for the adapter.
     * When edit mode is enabled, clicking on a routine will open the edit routine fragment.
     *
     * @param editMode True to enable edit mode, false to disable.
     */
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public HomePageRoutineListAdapter(Context context,
        List<Routine> routines, HomePageRoutineFragment fragment, MainViewModel activityModel) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(routines));
        this.fragment = fragment;
        this.activityModel = fragment.getActivityModel();

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the task for this position.
        var routine = getItem(position);
        assert routine != null;


        // Check if a view is being reused...
        ListItemRoutineBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemRoutineBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemRoutineBinding.inflate(layoutInflater, parent, false);
        }

        // Apply color based on delete mode
        if (deleteMode) {
            binding.routineNameBtn.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.delete_red)));
        }else if (editMode){
            binding.routineNameBtn.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.edit_orange)));
        }else{
            binding.routineNameBtn.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.habitizer_blue)));
        }

        binding.routineNameBtn.setText(routine.name());
        binding.routineNameBtn.setOnClickListener(v->{
            if (!deleteMode && !editMode){
                Log.d("Routine selected", routine.id().toString());
                openTaskListFragment(routine.id());
            }else if(deleteMode){
                Log.d("Routine id for deleteMode", routine.id().toString());
                var dialogFragment = ConfirmDeleteRoutineDialogFragment.newInstance(routine.id());
                dialogFragment.show(fragment.getParentFragmentManager(), "ConfirmDeleteRoutineDialogFragment");
            }else{
                Log.d("Editing Routine", routine.id().toString());
                openEditRoutineTasksFragment(routine.id());
            }
        });

        return binding.getRoot();
    }

    private void openTaskListFragment(int routineID) {
        fragment.requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, TaskListFragment.newInstance(routineID))  // Make sure this ID matches your container
                .addToBackStack(null)  // Allows back navigation
                .commit();
    }

    private void openEditRoutineTasksFragment(int routineID) {
        fragment.requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, EditRoutineTasksFragment.newInstance(routineID))
                .addToBackStack(null)
                .commit();
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
