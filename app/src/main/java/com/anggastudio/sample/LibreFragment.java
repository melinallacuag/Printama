package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anggastudio.sample.WebApiSVEN.Controllers.AppSvenAPI;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Parameters.GlobalInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LibreFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libre, container, false);

        Button btnactivar   = view.findViewById(R.id.btnlibresi);
        Button btnnoactivar = view.findViewById(R.id.btnlibreno);

        btnnoactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "NO SE ACTIVO EL MODO LIBRE", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        btnactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar_modolibre(GlobalInfo.getPistola10.toString());
                Toast.makeText(getContext(), "SE ACTIVO EL MODO LIBRE", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }
    private void guardar_modolibre(String manguera){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.9:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AppSvenAPI appSvenAPI = retrofit.create(AppSvenAPI.class);

        final Picos picos = new Picos(manguera,"01","1","05","DB5","G",9999.00);

        Call<Picos> call = appSvenAPI.postPicos(picos);

        call.enqueue(new Callback<Picos>() {
            @Override
            public void onResponse(Call<Picos> call, Response<Picos> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Codigo de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Picos> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión APICORE", Toast.LENGTH_SHORT).show();
            }
        });

    }
}