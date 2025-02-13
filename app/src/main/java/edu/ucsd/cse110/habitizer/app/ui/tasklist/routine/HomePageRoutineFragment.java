package edu.ucsd.cse110.habitizer.app.ui.tasklist.routine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.habitizer.app.R;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentHomepageRoutineBinding;


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage_routine, container, false);
    }
}