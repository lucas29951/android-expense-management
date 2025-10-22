package com.labdevs.controldegastos;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.labdevs.controldegastos.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Fragment> fragments = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        beginFragmentTransaction();
    }

    private void beginFragmentTransaction() {
        loadFragments();
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_1) {
                // setCurrentFragment(fragments.get(0));
            }
            if (item.getItemId() == R.id.item_2) {
                // setCurrentFragment(fragments.get(1));
            }
            if (item.getItemId() == R.id.item_3) {
                setCurrentFragment(fragments.get(2));
            }
            if (item.getItemId() == R.id.item_4) {
                setCurrentFragment(fragments.get(3));
            }
            return true;
        });
    }

    public void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,fragment).commit();
    }

    private void loadFragments() {
        fragments.add(new BlankFragment());
        fragments.add(new BlankFragment());
        fragments.add(new CategoriasFragment());
        fragments.add(new InformeFragment());
    }
}