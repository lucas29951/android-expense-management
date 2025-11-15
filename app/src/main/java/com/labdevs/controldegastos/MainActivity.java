package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.labdevs.controldegastos.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final List<Fragment> fragments = new ArrayList<>();
    private AppViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.topAppBar);

        viewModel.getAppBarTitle().observe(this, title -> binding.topAppBar.setTitle(title));
        viewModel.getAppBarNavIcon().observe(this, this::setupAppBarNavIcon);

        setContentView(binding.getRoot());

        setupFragmentTransaction();
    }

    private void setupFragmentTransaction() {
        beginFragmentTransaction();
        setCurrentFragment(fragments.get(0));
    }


    private void setupAppBarNavIcon(Boolean enableIcon) {
        if (enableIcon) {
            binding.topAppBar.setNavigationIcon(R.drawable.close_24px);
            binding.topAppBar.setNavigationOnClickListener(v -> {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    // Pop the current fragment off the stack
                    getSupportFragmentManager().popBackStack();
                } else {
                    // Default back action if no fragments are in the back stack
                    getOnBackPressedDispatcher().onBackPressed();
                }
                viewModel.setModificarCuenta(false);
            });
        } else {
            binding.topAppBar.setNavigationIcon(null);
        }

    }

    private void beginFragmentTransaction() {
        loadFragments();
        setCurrentFragment(fragments.get(0));
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            }
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).addToBackStack(null).commit();
    }

    private void loadFragments() {
        fragments.add(new ResumeFragment());
        fragments.add(new AccountsFragment());
        fragments.add(new CategoriasFragment());
        fragments.add(new InformeFragment());
        fragments.add(new SettingsFragment());
    }

}