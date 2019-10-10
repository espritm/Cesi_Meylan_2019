package com.example.myapplication.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    HomeFragment HomeFragment;
    BottomNavigationView NavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavView = findViewById(R.id.navigation);
        NavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return manageNavigationClick(menuItem.getItemId());
            }
        });

        //Recupere le login de l'utilisateur
        String sLogin = getIntent().getStringExtra("userLogin");

        //L'enregistre dans les SharedPreferences de l'app
        getPreferences(MODE_PRIVATE)
                .edit()
                .putString("connectedUserLogin", sLogin)
                .apply();

        //Affiche le fragment home par defaut
        manageNavigationClick(R.id.menu_home);
    }

    private boolean manageNavigationClick(int itemID){
        if (itemID == R.id.menu_home){
            HomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("tagFragmentHome");
            if (HomeFragment == null)
                HomeFragment = new HomeFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, HomeFragment, "tagFragmentHome").commit();
            return true;
        }
        else if (itemID == R.id.menu_preferences) {
            Toast.makeText(MainActivity.this, "Bientôt disponible :-)", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == HomeFragment.GEOLOCATION_PERMISSION_REQUEST_CODE && grantResults != null){

            //L'utilisateur vient d'accepter ou refuser de donner l'autorisation de geoLoc

            boolean bHasGivenAuthorization = true;
            for (int iPermission: grantResults) {
                bHasGivenAuthorization = bHasGivenAuthorization && iPermission == PackageManager.PERMISSION_GRANTED;
            }

            if (bHasGivenAuthorization){
                //User has allowed geoloc. Refresh data from Fragment !
                Fragment f = getSupportFragmentManager().findFragmentByTag("tagFragmentHome");
                HomeFragment homeFragment = null;

                try {
                    if (f != null)
                        homeFragment = (HomeFragment) f;
                }
                catch (Exception e){
                    //Nothing to do..
                }

                if (homeFragment != null)
                    homeFragment.updateDataWithGeolocation();
            }
        }
    }
}