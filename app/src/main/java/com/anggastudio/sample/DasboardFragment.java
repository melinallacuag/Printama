package com.anggastudio.sample;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.BreakIterator;


public class DasboardFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dasboard, container, false);
        CardView btnventa       = view.findViewById(R.id.btnventa);
        CardView btncierrex     = view.findViewById(R.id.btnCierreX);
        CardView btncambioturno = view.findViewById(R.id.btnCambioTurno);
        CardView btniniciodia   = view.findViewById(R.id.btnInicioDia);
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,cambioTurnoFragment).commit();
            }
        });
        btniniciodia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InicioDiaFragment inicioDiaFragment = new InicioDiaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,inicioDiaFragment).commit();
            }
        });
       return view;
    }


}