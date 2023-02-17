package com.anggastudio.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class SalirFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_salir, container, false);
        Button btncancelar = view.findViewById(R.id.btncancelarsalida);
        Button btnsalir  = view.findViewById(R.id.btnsalir);

        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "SE CANCELO", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        btnsalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Toast.makeText(getContext(), "CERRAR SESIÓN", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}