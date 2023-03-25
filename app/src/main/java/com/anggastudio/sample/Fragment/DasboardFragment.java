package com.anggastudio.sample.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.sample.Login;
import com.anggastudio.sample.R;
import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Company;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DasboardFragment extends Fragment{

    TextView nombreusuario,fecha,turno,nombregrigo,sucursal,slogangrifo;
    CardView btnventa,btncierrex,btncambioturno,btniniciodia;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    Button btncancelar,btnagregar;

    private APIService mAPIService;

    private Dialog modalCambioTurno,modalIcioDia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dasboard, container, false);

        mAPIService = GlobalInfo.getAPIService();

        btnventa       = view.findViewById(R.id.btnventa);
        btncierrex     = view.findViewById(R.id.btnCierreX);
        btncambioturno = view.findViewById(R.id.btnCambioTurno);
        btniniciodia   = view.findViewById(R.id.btnInicioDia);

        nombreusuario = view.findViewById(R.id.nombreuser);
        fecha         = view.findViewById(R.id.fecha);
        turno         = view.findViewById(R.id.turno);

        nombregrigo   = view.findViewById(R.id.nombregrigo);
        sucursal      = view.findViewById(R.id.nombresucursal);
        slogangrifo   = view.findViewById(R.id.slogangrifo);

        nombreusuario.setText(GlobalInfo.getuserName10);
        fecha.setText(GlobalInfo.getterminalFecha10);
        turno.setText(GlobalInfo.getterminalTurno10.toString());


        findCompany(GlobalInfo.getterminalCompanyID10);

        btnventa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VentaFragment ventaFragment = new VentaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ventaFragment).commit();
            }
        });

        btncierrex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CierreXFragment cierreXFragment = new CierreXFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,cierreXFragment).commit();
            }
        });

        /** Mostrar Modal de Cambio de Turno */
        modalCambioTurno = new Dialog(getContext());
        modalCambioTurno.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalCambioTurno.setContentView(R.layout.fragment_cambio_turno);
        modalCambioTurno.setCancelable(false);

        btncambioturno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modalCambioTurno.show();

                btncancelar    = modalCambioTurno.findViewById(R.id.btncancelarcambioturno);
                btnagregar     = modalCambioTurno.findViewById(R.id.btnagregarcambioturno);

                btncancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalCambioTurno.dismiss();
                    }
                });
                btnagregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            Intent intent = new Intent(getContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            finalize();
                            Toast.makeText(getContext(), "SE GENERO EL CAMBIO DE TURNO ", Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


        /** Mostrar Modal de Inicio de Día */
        modalIcioDia = new Dialog(getContext());
        modalIcioDia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modalIcioDia.setContentView(R.layout.fragment_inicio_dia);
        modalIcioDia.setCancelable(false);

        btniniciodia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modalIcioDia.show();

                btncancelar    = modalIcioDia.findViewById(R.id.btncancelariniciodia);
                btnagregar     = modalIcioDia.findViewById(R.id.btnagregariniciodia);

                btncancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalIcioDia.dismiss();
                    }
                });
                btnagregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            finalize();
                            Toast.makeText(getContext(), "SE GENERO EL INICIO DE DÍA", Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
              }
        });

       return view;

    }

    private void findCompany(Integer id){

        Call<List<Company>> call = mAPIService.findCompany(id);

        call.enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                try {

                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Company> companyList = response.body();

                    for(Company company: companyList){

                        GlobalInfo.getNameCompany10    = String.valueOf(company.getNames());
                        GlobalInfo.getRucCompany10     = String.valueOf(company.getRuc());
                        GlobalInfo.getAddressCompany10 = String.valueOf(company.getAddress());
                        GlobalInfo.getBranchCompany10  = String.valueOf(company.getBranch());
                        GlobalInfo.getPhoneCompany10   = String.valueOf(company.getPhone());
                        GlobalInfo.getMainCompany10    = String.valueOf(company.getMail());
                        GlobalInfo.getManagerCompany10 = String.valueOf(company.getManager());
                        GlobalInfo.getSloganCompany10  = String.valueOf(company.getEslogan());

                        String DirSucursal = company.getBranch();

                        DirSucursal = DirSucursal.replace("-","");

                        nombregrigo.setText(company.getNames());
                        sucursal.setText(DirSucursal.toString());
                        slogangrifo.setText(company.getEslogan());

                    }

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE - RED - WIFI", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void abrirmodal(){
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

}