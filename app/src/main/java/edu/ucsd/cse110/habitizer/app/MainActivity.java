package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.routine.RoutineListFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;

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
                    .replace(R.id.fragmentContainerView, RoutineListFragment.newInstance())
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
            swapToMainMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void swapToMainMenu(){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, RoutineListFragment.newInstance())
                    .commit();
    }

}
