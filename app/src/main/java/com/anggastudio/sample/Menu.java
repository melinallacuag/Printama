package com.anggastudio.sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anggastudio.sample.Fragment.CierreXFragment;
import com.anggastudio.sample.Fragment.DasboardFragment;
import com.anggastudio.sample.Fragment.VentaFragment;
import com.google.android.material.navigation.NavigationView;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Button btncancelar,btnagregar,btnsalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout   = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);

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

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DasboardFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_venta:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VentaFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_cierrex:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CierreXFragment()).addToBackStack(null).commit();
                break;

            case R.id.nav_cambioturno:

                builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogCambioturno = inflater.inflate(R.layout.fragment_cambio_turno, null);
                builder.setView(dialogCambioturno);
                abrirmodal();

                btncancelar    = dialogCambioturno.findViewById(R.id.btncancelarcambioturno);
                btnagregar     = dialogCambioturno.findViewById(R.id.btnagregarcambioturno);

                btncancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btnagregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finalize();
                            Toast.makeText(getApplicationContext(), "SE GENERO EL CAMBIO DE TURNO", Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;

            case R.id.nav_iniciodia:
                builder = new AlertDialog.Builder(this);
                LayoutInflater inflate = this.getLayoutInflater();
                View dialogIniciodia = inflate.inflate(R.layout.fragment_inicio_dia, null);
                builder.setView(dialogIniciodia);
                abrirmodal();

                btncancelar    = dialogIniciodia.findViewById(R.id.btncancelariniciodia);
                btnagregar     = dialogIniciodia.findViewById(R.id.btnagregariniciodia);

                btncancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btnagregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finalize();
                            Toast.makeText(getApplicationContext(), "SE GENERO EL INICIO DE DÍA", Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.nav_salir:

                builder = new AlertDialog.Builder(this);
                LayoutInflater inflaters = this.getLayoutInflater();
                View dialogSalir = inflaters.inflate(R.layout.fragment_salir, null);
                builder.setView(dialogSalir);
                abrirmodal();

                btncancelar    = dialogSalir.findViewById(R.id.btncancelarsalida);
                btnsalir     = dialogSalir.findViewById(R.id.btnsalir);

                btncancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btnsalir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finalize();
                            Toast.makeText(getApplicationContext(), "CERRAR SESIÓN", Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DasboardFragment()).commit();
        }



    }

    private void abrirmodal(){
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
    }
}