package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucsd.cse110.habitizer.app.R;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomepageRoutineBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListEveningFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListMorningFragment;


public class HomePageRoutineFragment extends Fragment {
    private FragmentHomepageRoutineBinding view;

    public HomePageRoutineFragment() {
        // Required empty public constructor
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_routine, container, false);

        Button btnMorningTasks = view.findViewById(R.id.morning_routine_button); //added for home buttons binding
        Button btnEveningTasks = view.findViewById(R.id.evening_routine_button);

        btnMorningTasks.setOnClickListener(v -> openFragment(new TaskListMorningFragment())); //routine button binding
        btnEveningTasks.setOnClickListener(v -> openFragment(new TaskListEveningFragment()));

        return view;
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)  // Make sure this ID matches your container
                .addToBackStack(null)  // Allows back navigation
                .commit();
    }


}