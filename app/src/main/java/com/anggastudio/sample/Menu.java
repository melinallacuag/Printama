package com.anggastudio.sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.anggastudio.sample.Fragment.CambioTurnoFragment;
import com.anggastudio.sample.Fragment.CierreXFragment;
import com.anggastudio.sample.Fragment.DasboardFragment;
import com.anggastudio.sample.Fragment.InicioDiaFragment;
import com.anggastudio.sample.Fragment.SalirFragment;
import com.anggastudio.sample.Fragment.VentaFragment;
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

            case R.id.nav_cambioturno:
                CambioTurnoFragment cambioTurnoFragment = new CambioTurnoFragment();
                cambioTurnoFragment.show(getSupportFragmentManager(), "Cambio de Turno");
                cambioTurnoFragment.setCancelable(false);
                break;

            case R.id.nav_iniciodia:
                InicioDiaFragment inicioDiaFragment = new InicioDiaFragment();
                inicioDiaFragment.show(getSupportFragmentManager(), "Inicio de Día");
                inicioDiaFragment.setCancelable(false);
                break;
            case R.id.nav_salir:
                 SalirFragment salirFragment = new SalirFragment();
                salirFragment.show(getSupportFragmentManager(), "Inicio de Día");
                salirFragment.setCancelable(false);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DasboardFragment()).commit();
    }




}