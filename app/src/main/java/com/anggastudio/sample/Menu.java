package com.anggastudio.sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DasboardFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_dasboard);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dasboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DasboardFragment()).commit();
                break;
            case R.id.nav_venta:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VentaFragment()).commit();
                break;
            case R.id.nav_cierrex:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CierreXFragment()).commit();
                break;
            case R.id.nav_cierrez:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CierreZFragment()).commit();
                break;
            case R.id.nav_cambioturno:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CambioTurnoFragment()).commit();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}