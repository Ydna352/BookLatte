/*
======Integrantes del equipo======
Martínez Arroyo Andrea
Paúl Ortega Naomi Astrid
Piedras Mora Jorge Bryan
*/

package com.andrea.booklatte;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andrea.booklatte.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DBHelper(this);

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView2.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.itemHome) {
                replaceFragment(new HomeFragment());
            }
            else if (id == R.id.itemMaps) {
                replaceFragment(new MapsFragment());
            }
            else if (id == R.id.itemProfile) {
                replaceFragment(new ProfileFragment());
            }
            return true;

        } );
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}