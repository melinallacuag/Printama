package com.anggastudio.sample;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;
import com.anggastudio.sample.WebApiSVEN.Models.Company;
import com.anggastudio.sample.WebApiSVEN.Models.Terminal;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DasboardFragment extends Fragment{

    TextView nombreusuario,fecha,turno,nombregrigo,sucursal,slogangrifo;

    private APIService mAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dasboard, container, false);
        CardView btnventa       = view.findViewById(R.id.btnventa);
        CardView btncierrex     = view.findViewById(R.id.btnCierreX);
        CardView btncambioturno = view.findViewById(R.id.btnCambioTurno);
        CardView btniniciodia   = view.findViewById(R.id.btnInicioDia);

        nombreusuario = view.findViewById(R.id.nombreuser);
        fecha         = view.findViewById(R.id.fecha);
        turno         = view.findViewById(R.id.turno);
        nombregrigo   = view.findViewById(R.id.nombregrigo);
        sucursal      = view.findViewById(R.id.nombresucursal);
        slogangrifo   = view.findViewById(R.id.slogangrifo);

        nombreusuario.setText(GlobalInfo.getName10);
        fecha.setText(GlobalInfo.getfecha10);
        turno.setText(GlobalInfo.getturno10.toString());

        mAPIService = GlobalInfo.getAPIService();

        findCompany(GlobalInfo.getCompanyID10);

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

        btncambioturno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambioTurnoFragment cambioTurnoFragment = new CambioTurnoFragment();
                cambioTurnoFragment.show(getActivity().getSupportFragmentManager(), "Cambio de Turno");
                cambioTurnoFragment.setCancelable(false);
            }
        });

        btniniciodia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InicioDiaFragment inicioDiaFragment = new InicioDiaFragment();
                inicioDiaFragment.show(getActivity().getSupportFragmentManager(), "Inicio de Día");
                inicioDiaFragment.setCancelable(false);
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

                        nombregrigo.setText(company.getNames());
                        sucursal.setText(company.getBranch());
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


}