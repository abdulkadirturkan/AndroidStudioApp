package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class IRCoordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_ircoord);
        //toolbar bölümü
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DuyuruFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_duyuru);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_duyuru:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DuyuruFragment()).commit();
                break;
            case R.id.nav_belge_coord:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BelgeCoordFragment()).commit();
                break;
            case R.id.nav_mufredat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MufredatFragment()).commit();
                break;
            case R.id.nav_coordinator:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CoordinatorFragment()).commit();
                break;
            case R.id.nav_ircoord_ogrenci:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new IRCoordinatorStuFragment()).commit();
                break;
            case R.id.nav_exit:
                startActivity(new Intent(this, MainActivity.class));

                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
