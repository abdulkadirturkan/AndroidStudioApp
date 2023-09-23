package com.example.project;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class CoordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //public class CoordActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_coord);
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
            case R.id.nav_ogrenciler:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OgrenciFragment()).commit();
                break;
            /*case R.id.nav_belge_coord:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BelgeCoordFragment()).commit();
                break;

             */
            case R.id.nav_university_coord:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UniversityCoordFragment()).commit();
                break;
            /*case R.id.nav_ders_coord:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LessonCoordFragment()).commit();
                break;*/
            case R.id.nav_mufredat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MufredatFragment()).commit();
                break;
            /*case R.id.nav_ogrenci_ders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OgrenciDersFragment()).commit();
                break;*/
            /*case R.id.nav_sohbet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SohbetFragment()).commit();
                break;

             */
            case R.id.nav_ogrenci_belge:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OgrenciBelgeFragment()).commit();
                break;


            case R.id.nav_statictic:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatisticFragment()).commit();
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
