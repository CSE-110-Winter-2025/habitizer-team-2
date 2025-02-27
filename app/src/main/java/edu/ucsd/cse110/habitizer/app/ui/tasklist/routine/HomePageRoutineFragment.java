package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ucsd.cse110.habitizer.app.R;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomepageRoutineBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListFragment;


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

        // Stores edit morning and evening routine buttons to use
        Button btnEditMornRtn = view.findViewById(R.id.edit_morning_rtn_btn);
        Button btnEditEvenRtn = view.findViewById(R.id.edit_evening_rtn_btn);

        btnMorningTasks.setOnClickListener(v -> openTaskListFragment(true)); //routine button binding
        btnEveningTasks.setOnClickListener(v -> openTaskListFragment(false));

        // Listens to user pressing edit morning or evening routine buttons
        // Sends in true or false depending on the button pressed to properly populate fragment with correct data
        btnEditMornRtn.setOnClickListener(v -> openEditRoutineTasksFragment(true));
        btnEditEvenRtn.setOnClickListener(v -> openEditRoutineTasksFragment(false));

        return view.getRootView();
    }

    private void openTaskListFragment(boolean isMorning) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, TaskListFragment.newInstance(0))  // Make sure this ID matches your container
                .addToBackStack(null)  // Allows back navigation
                .commit();
    }

    // Specific fragment intended for detecting if evening or morning routine is chosen to edit
    // returns true if morning routine is chosen
    private void openEditRoutineTasksFragment(boolean isMorning) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, EditRoutineTasksFragment.newInstance(isMorning)) // boolean parameter for instance of morning or evening routine
                .addToBackStack(null)
                .commit();
    }


}