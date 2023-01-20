package com.anggastudio.sample;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


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
                Toast.makeText(getContext(), "SE ACTIVO EL MODO LIBRE", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }
}