package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
// import android.widget.Toolbar;

public class studentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        //toolbar bölümü
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Drawer'bölümü
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //Ogrenci Girisinin ana fragmenti Profil
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_university:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UniversityFragment()).commit();
                break;
            case R.id.nav_ders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DersFragment()).commit();
                break;
            case R.id.nav_duyuru:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StuDuyuruFragment()).commit();
                break;
            case R.id.nav_belge:
                startActivity(new Intent(studentActivity.this, BelgeFragment.class));
                break;
            case R.id.nav_sohbet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StuSohbetPreFragment()).commit();
                break;
            case R.id.nav_exit:
                startActivity(new Intent(this, MainActivity.class));
                //new Intent(studentActivity.this, LoginStu.class);
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