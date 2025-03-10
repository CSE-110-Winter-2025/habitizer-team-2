package edu.ucsd.cse110.habitizer.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
//import edu.ucsd.cse110.habitizer.app.ui.tasklist.routine.EditRoutineListFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.routine.HomePageRoutineFragment;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.task.TaskListFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;

    SharedPreferences sharedPreferences;

    private int routineID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        sharedPreferences = getSharedPreferences("habitizer", MODE_PRIVATE);
        this.routineID = sharedPreferences.getInt("routineID", -1);

        // Show HomePageRoutineFragment first on app launch
        if (savedInstanceState == null) { // Prevent reloading on rotation
            if (this.routineID == -1){
                Log.d("Routine selected", Integer.toString(this.routineID));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, HomePageRoutineFragment.newInstance())
                        .commit();
            }else{
                Log.d("Routine selected", Integer.toString(this.routineID));
                openTaskListFragment(this.routineID);
            }
        }

    }

    private void openTaskListFragment(int routineID) {
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, TaskListFragment.newInstance(routineID))  // Make sure this ID matches your container
                .addToBackStack(null)  // Allows back navigation
                .commit();
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
            swapToMainMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void swapToMainMenu(){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, HomePageRoutineFragment.newInstance())
                    .commit();
    }

}
