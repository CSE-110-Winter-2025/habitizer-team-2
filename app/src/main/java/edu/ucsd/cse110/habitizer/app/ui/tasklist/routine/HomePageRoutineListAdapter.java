package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

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
import edu.ucsd.cse110.habitizer.app.databinding.ListItemRoutineBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class HomePageRoutineListAdapter extends ArrayAdapter<Routine> {
    MainViewModel activityModel;
    HomePageRoutineFragment fragment;

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

        // Populate the view with the task's data.
        binding.routineNameBtn.setText(routine.name());
        binding.routineNameBtn.setOnClickListener(v->{openTaskListFragment(routine.id());});

        return binding.getRoot();
    }

    private void openTaskListFragment(int routineID) {
        fragment.requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, TaskListFragment.newInstance(routineID))  // Make sure this ID matches your container
                .addToBackStack(null)  // Allows back navigation
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
