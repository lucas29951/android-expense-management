package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.labdevs.controldegastos.data.database.Converters;
import com.labdevs.controldegastos.data.entity.Transaccion;
import com.labdevs.controldegastos.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Fragment> fragments = new ArrayList<>();
    private AppViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        viewModel.getAppBarTitle().observe(this,title -> binding.topAppBar.setTitle(title));

        setContentView(binding.getRoot());

        beginFragmentTransaction();
    }

    private void beginFragmentTransaction() {
        loadFragments();
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_1) {
                setCurrentFragment(fragments.get(0));
            }
            if (item.getItemId() == R.id.item_2) {
                setCurrentFragment(fragments.get(1));
            }
            if (item.getItemId() == R.id.item_3) {
                setCurrentFragment(fragments.get(2));
            }
            if (item.getItemId() == R.id.item_4) {
                setCurrentFragment(fragments.get(3));
            }
            if (item.getItemId() == R.id.item_5) {
                setCurrentFragment(fragments.get(4));
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit();
    }

    private void loadFragments() {
        fragments.add(new ResumeFragment());
        fragments.add(new AccountsFragment());
        fragments.add(new BlankFragment());
        fragments.add(new InformeFragment());
        fragments.add(new SettingsFragment());
    }
}