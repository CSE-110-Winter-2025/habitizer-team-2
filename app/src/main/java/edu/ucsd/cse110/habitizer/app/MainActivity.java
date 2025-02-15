package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.EditRoutineTasksFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListMorningFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.routine.HomePageRoutineFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private boolean isShowingRoutine = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        // Show HomePageRoutineFragment first on app launch
        if (savedInstanceState == null) { // Prevent reloading on rotation
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, HomePageRoutineFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    // Transitions to screen for editing tasks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        if (itemId == R.id.action_bar_menu_swap_views) {
            // TODO: Swap the views.
            swapFragments();

        }

        return super.onOptionsItemSelected(item);
    }

    private void swapFragments(){
        if(isShowingRoutine){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, HomePageRoutineFragment.newInstance())
                    .commit();
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, TaskListMorningFragment.newInstance())
                    .commit();
        }
        isShowingRoutine = !isShowingRoutine;
    }

}
