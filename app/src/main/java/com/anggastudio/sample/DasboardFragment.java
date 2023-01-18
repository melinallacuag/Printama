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

    TextView totalmonto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dasboard, container, false);
        CardView btnventa = view.findViewById(R.id.btnventa);
        btnventa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VentaFragment ventaFragment = new VentaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ventaFragment).commit();
            }
        });
       return view;
    }


}