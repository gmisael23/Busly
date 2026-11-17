package com.busly.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.busly.app.databinding.ActivityMainBinding;
import com.busly.app.ui.AlertsFragment;
import com.busly.app.ui.HistoryFragment;
import com.busly.app.ui.HomeFragment;

/** Container das telas Início, Alertas e Histórico, com navegação inferior. */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment;
            if (id == R.id.nav_inicio) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_alertas) {
                fragment = new AlertsFragment();
            } else {
                fragment = new HistoryFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            binding.bottomNav.setSelectedItemId(R.id.nav_inicio);
        }
    }
}
