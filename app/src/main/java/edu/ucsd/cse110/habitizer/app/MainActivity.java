package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.io.Console;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.TaskListFragment;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }
}
